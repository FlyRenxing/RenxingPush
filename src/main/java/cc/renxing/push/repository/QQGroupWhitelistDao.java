package cc.renxing.push.repository;

import cc.renxing.push.model.po.QQGroupWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Renxing
 */
@Repository
public interface QQGroupWhitelistDao extends JpaRepository<QQGroupWhitelist, Long> {

    List<QQGroupWhitelist> findByNumber(Long qqGroup);

    List<QQGroupWhitelist> findByUserId(Long uid);
}
