package top.imzdx.qqpush.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.qqpush.model.po.MessageCallback;

import java.util.List;

@Repository
public interface MessageCallbackDao extends JpaRepository<MessageCallback, Long> {

    List<MessageCallback> findAll(Example example);

    List<MessageCallback> findDistinctSenderBy();
}
