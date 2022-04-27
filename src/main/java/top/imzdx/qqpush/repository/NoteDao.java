package top.imzdx.qqpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.qqpush.model.po.Note;

/**
 * @author Renxing
 */
@Repository
public interface NoteDao extends JpaRepository<Note, Long> {

}
