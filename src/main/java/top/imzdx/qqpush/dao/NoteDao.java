package top.imzdx.qqpush.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.imzdx.qqpush.model.po.Note;

import java.util.List;

/**
 * @author Renxing
 */
@Mapper
public interface NoteDao {
    @Select("SELECT * FROM `note` ")
    List<Note> findAllNote();
}
