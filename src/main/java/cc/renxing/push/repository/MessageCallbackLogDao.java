package cc.renxing.push.repository;

import cc.renxing.push.model.po.MessageCallbackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageCallbackLogDao extends JpaRepository<MessageCallbackLog, Long> {
}
