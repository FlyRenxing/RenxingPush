package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Renxing
 */
@Mapper
public interface MessageLogDao {
    @Insert("INSERT INTO `qqmsg`.`message_log`(`content`, `meta`, `uid`) VALUES (#{content}, #{meta}, #{uid})")
    int InsertMessageLog(@Param("content") String content, @Param("meta") String meta, @Param("uid") long uid);

    @Select("SELECT COUNT(1) FROM message_log WHERE uid=#{uid} AND DATE_FORMAT( time, '%Y%m%d' ) = DATE_FORMAT( CURDATE() , '%Y%m%d' )")
    int SelectToDayUserUseCount(long uid);

    @Select("SELECT COUNT(1) FROM message_log WHERE uid=1 AND time >= DATE_SUB(now(), interval 3 SECOND)")
    int SelectThreeSecondUserUseCount(long uid);
}
