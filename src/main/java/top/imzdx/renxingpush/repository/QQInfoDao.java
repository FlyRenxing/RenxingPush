package top.imzdx.renxingpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.renxingpush.model.po.QQInfo;

import java.util.Optional;

/**
 * @author Renxing
 */
@Repository
public interface QQInfoDao extends JpaRepository<QQInfo, Long> {

    Optional<QQInfo> findFirstBy();

    Optional<QQInfo> findByNumber(long number);
}
