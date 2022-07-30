package top.imzdx.renxingpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.renxingpush.model.po.MessageLog;

@Repository
public interface MessageLogDao extends JpaRepository<MessageLog, Long> {
}
