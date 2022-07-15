package top.imzdx.qqpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.qqpush.model.po.MessageCallbackLog;

@Repository
public interface MessageCallbackLogDao extends JpaRepository<MessageCallbackLog, Long> {
}
