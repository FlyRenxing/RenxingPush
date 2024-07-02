package cc.renxing.push.repository;

import cc.renxing.push.model.po.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Renxing
 */
@Repository
public interface NoteDao extends JpaRepository<Note, Long> {

}
