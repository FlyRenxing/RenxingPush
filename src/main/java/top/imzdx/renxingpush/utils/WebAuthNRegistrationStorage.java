package top.imzdx.renxingpush.utils;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.UserDao;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebAuthNRegistrationStorage implements CredentialRepository {
    UserDao userDao;

    @Autowired
    public WebAuthNRegistrationStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Get the credential IDs of all credentials registered to the user with the given username.
     *
     * <p>After a successful registration ceremony, the {@link RegistrationResult#getKeyId()} method
     * returns a value suitable for inclusion in this set.
     *
     * @param username 用户名
     */
    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        return userDao.findWithWebAuthNCredentialsByName(username).orElseThrow(() -> {
                    throw new DefinitionException("用户不存在");
                })
                .getWebAuthNCredentials().stream().map(
                        webAuthNCredential -> PublicKeyCredentialDescriptor.builder()
                                .id(ByteArray.fromBase64(webAuthNCredential.getKeyId()))
                                .build())
                .collect(Collectors.toSet());
    }

    /**
     * Get the user handle corresponding to the given username - the inverse of {@link
     * #getUsernameForUserHandle(ByteArray)}.
     *
     * <p>Used to look up the user handle based on the username, for authentication ceremonies where
     * the username is already given.
     *
     * @param username 用户名
     */
    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        String webauthnHandle = userDao.findByName(username).orElseThrow(() -> {
                    throw new DefinitionException("用户不存在");
                })
                .getWebauthnHandle();
        return Optional.of(ByteArray.fromBase64(webauthnHandle));
    }

    /**
     * Get the username corresponding to the given user handle - the inverse of {@link
     * #getUserHandleForUsername(String)}.
     *
     * <p>Used to look up the username based on the user handle, for username-less authentication
     * ceremonies.
     *
     * @param userHandle 用户句柄
     */
    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        User user = userDao.findByWebauthnHandle(userHandle.getBase64()).orElseThrow(() -> {
            throw new DefinitionException("用户不存在");
        });
        return Optional.of(String.valueOf(user.getUid()));
    }

    /**
     * Look up the public key and stored signature count for the given credential registered to the
     * given user.
     *
     * <p>The returned {@link RegisteredCredential} is not expected to be long-lived. It may be read
     * directly from a database or assembled from other components.
     *
     * @param credentialId 凭据id
     * @param userHandle   用户句柄
     */
    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        return userDao.findWithWebAuthNCredentialsByWebauthnHandle(userHandle.getBase64()).stream().filter(
                        user1 -> user1.getWebAuthNCredentials().stream().anyMatch(
                                webAuthNCredential -> webAuthNCredential.getKeyId().equals(credentialId.getBase64())
                        )
                ).findFirst().orElseThrow(() -> {
                    throw new DefinitionException("该凭证找不到绑定的用户");
                }).getWebAuthNCredentials().stream().filter(webAuthNCredential -> webAuthNCredential.getKeyId().equals(credentialId.getBase64())).findFirst()
                .map(webAuthNCredential -> RegisteredCredential.builder()
                        .credentialId(credentialId)
                        .userHandle(userHandle)
                        .publicKeyCose(ByteArray.fromBase64(webAuthNCredential.getPublicKeyCose()))
                        .signatureCount(webAuthNCredential.getSignatureCount())
                        .build()
                );
    }

    /**
     * Look up all credentials with the given credential ID, regardless of what user they're
     * registered to.
     *
     * <p>This is used to refuse registration of duplicate credential IDs. Therefore, under normal
     * circumstances this method should only return zero or one credential (this is an expected
     * consequence, not an interface requirement).
     *
     * @param credentialId 凭据id
     */
    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        return userDao.findWithWebAuthNCredentialsByWebAuthNCredentialsKeyId(credentialId.getBase64()).stream().flatMap(
                user -> user.getWebAuthNCredentials().stream()
        ).map(webAuthNCredential -> RegisteredCredential.builder()
                .credentialId(ByteArray.fromBase64(webAuthNCredential.getKeyId()))
                .userHandle(ByteArray.fromBase64(userDao.findByWebauthnHandle(webAuthNCredential.getUser().getWebauthnHandle())
                        .orElseThrow(() -> {
                            throw new DefinitionException("用户不存在");
                        })
                        .getWebauthnHandle()))
                .publicKeyCose(ByteArray.fromBase64(webAuthNCredential.getPublicKeyCose()))
                .signatureCount(webAuthNCredential.getSignatureCount())
                .build()
        ).collect(Collectors.toSet());
    }
}