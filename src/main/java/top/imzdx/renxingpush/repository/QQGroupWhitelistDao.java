package top.imzdx.renxingpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.renxingpush.model.po.QQGroupWhitelist;

import java.util.List;

/**
 * @author Renxing
 */
@Repository
public interface QQGroupWhitelistDao extends JpaRepository<QQGroupWhitelist, Long> {

    List<QQGroupWhitelist> findByNumber(Long qqGroup);

    List<QQGroupWhitelist> findByUserId(Long uid);
}
