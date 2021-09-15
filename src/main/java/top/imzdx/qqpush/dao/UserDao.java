package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.*;
import top.imzdx.qqpush.model.po.User;

import java.util.List;

/**
 * @author Renxing
 */
@Mapper
public interface UserDao {
    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("select * from user where name=#{name} limit 1")
    User findUserByName(String name);

    @Select("select * from user where uid=#{uid} limit 1")
    User findUserByUid(long uid);

    @Select("select * from user where cipher=#{cipher} limit 1")
    User findUserByCipher(String cipher);

    @Insert("INSERT INTO `qqmsg`.`user`(`name`, `password`, `config`, `cipher`) VALUES (#{user.name}, #{user.password}, #{user.config}, #{user.cipher})")
    int insertUser(@Param("user") User user);

    @Update("update `qqmsg`.`user` SET `name` = #{user.name}, `password` = #{user.password},`admin` = #{user.admin}, `config`= #{user.config},`cipher`=#{user.cipher} WHERE `uid` = #{user.uid}")
    int updateUser(@Param("user") User user);

    @Select("SELECT COUNT(1) FROM message_log WHERE uid=#{uid} AND DATE_FORMAT( time, '%Y%m%d' ) = DATE_FORMAT( CURDATE() , '%Y%m%d' )")
    int selectToDayUserUseCount(long uid);

    @Select("SELECT COUNT(1) FROM message_log WHERE uid=#{uid} AND time >= DATE_SUB(now(), interval 3 SECOND)")
    int selectThreeSecondUserUseCount(long uid);

}
