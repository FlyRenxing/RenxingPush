package top.imzdx.renxingpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.renxingpush.model.po.Note;

/**
 * @author Renxing
 */
@Repository
public interface NoteDao extends JpaRepository<Note, Long> {

}
