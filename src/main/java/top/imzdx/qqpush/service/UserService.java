package top.imzdx.qqpush.service;


import top.imzdx.qqpush.model.po.User;

/**
 * @author Renxing
 */
public interface UserService {
    boolean register(String name, String password);

    String refreshCipher(String userName);

    User findUserByName(String name);
}
