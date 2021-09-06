package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.imzdx.qqpush.model.dto.Msg;

/**
 * @author Renxing
 */
@Mapper
public interface MessageLogDao {
    @Insert("INSERT INTO `qqmsg`.`message_log`(`content`, `meta`, `uid`) VALUES (#{content}, #{meta}, #{uid})")
    int InsertMessageLog(@Param("content") String content ,@Param("meta")String meta, @Param("uid") long uid);
}
