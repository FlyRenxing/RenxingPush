package top.imzdx.qqpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;

import java.util.List;

/**
 * @author Renxing
 */
@Repository
public interface QQGroupWhitelistDao extends JpaRepository<QQGroupWhitelist, Long> {

    List<QQGroupWhitelist> findByNumber(Long qqGroup);
}
