package top.imzdx.qqpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.qqpush.model.po.QQInfo;

import java.util.Optional;

/**
 * @author Renxing
 */
@Repository
public interface QQInfoDao extends JpaRepository<QQInfo, Long> {

    Optional<QQInfo> findFirstBy();

    Optional<QQInfo> findByNumber(long number);
}
