package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.imzdx.qqpush.model.po.QqInfo;

import java.util.List;

/**
 * @author Renxing
 */
@Mapper
public interface QqInfoDao {
    @Select("SELECT * FROM qq_info")
    List<QqInfo> findAll();

    @Select("SELECT * FROM qq_info limit 1")
    QqInfo getFirst();

    @Update("UPDATE `qq_info` SET `state` = #{state} WHERE `number` = #{number}")
    int updateState(@Param("number") long number, @Param("state") long state);

    @Select("SELECT * FROM qq_info where number=#{number} limit 1")
    QqInfo findInfoByNumber(long number);
}
