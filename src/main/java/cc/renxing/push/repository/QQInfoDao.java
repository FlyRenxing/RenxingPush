package cc.renxing.push.repository;

import cc.renxing.push.model.po.QQInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Renxing
 */
@Repository
public interface QQInfoDao extends JpaRepository<QQInfo, Long> {

    Optional<QQInfo> findFirstBy();

    Optional<QQInfo> findFirstByState(int state);

    Optional<QQInfo> findByNumber(long number);
}
