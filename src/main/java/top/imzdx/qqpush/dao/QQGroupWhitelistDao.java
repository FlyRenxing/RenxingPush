package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.*;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;

import java.util.List;

/**
 * @author Renxing
 */
@Mapper
public interface QQGroupWhitelistDao {
    @Select("SELECT * FROM qq_group_whitelist")
    List<QQGroupWhitelist> findAll();

    @Select("SELECT * FROM qq_group_whitelist where number=#{number} limit 1")
    QQGroupWhitelist findGroupByNumber(long number);

    @Insert("INSERT INTO `qq_group_whitelist`(`number`, `user_id`) VALUES (#{whitelist.number}, #{whitelist.userId})")
    Boolean insert(@Param("whitelist")QQGroupWhitelist qqGroupWhitelist);
}
