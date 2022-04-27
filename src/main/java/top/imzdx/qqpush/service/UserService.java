package top.imzdx.qqpush.service;


import top.imzdx.qqpush.model.po.User;

/**
 * @author Renxing
 */
public interface UserService {
    boolean register(String name, String password);

    boolean register(String name, String password, String openid);

    String refreshCipher(String userName);

    User findUserByName(String name);

    User findUserById(Long id);

    User findUserByOpenid(String openid);

    boolean setQQBot(long uid, long number);

    int selectToDayUserUseCount(long uid);

}
