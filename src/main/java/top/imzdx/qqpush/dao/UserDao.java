package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.imzdx.qqpush.model.po.User;

import java.util.List;

/**
 * @author Renxing
 */
@Mapper
public interface UserDao {
    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("select * from user where name=#{name}")
    User findUserByName(String name);

    @Select("select * from user where cipher=#{cipher}")
    User findUserByCipher(String cipher);

    @Insert("INSERT INTO `qqmsg`.`user`(`name`, `password`, `config`, `cipher`) VALUES (#{user.name}, #{user.password}, #{user.config}, #{user.cipher})")
    int InsertUser(@Param("user") User user);
}
