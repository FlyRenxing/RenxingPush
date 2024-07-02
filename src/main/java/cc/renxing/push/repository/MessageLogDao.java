package cc.renxing.push.repository;

import cc.renxing.push.model.po.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogDao extends JpaRepository<MessageLog, Long> {
}
