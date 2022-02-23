package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Renxing
 */
@Mapper
public interface MessageLogDao {
    @Insert("INSERT INTO `message_log`(`content`, `meta`, `uid`) VALUES (#{content}, #{meta}, #{uid})")
    int InsertMessageLog(@Param("content") String content, @Param("meta") String meta, @Param("uid") long uid);
}
