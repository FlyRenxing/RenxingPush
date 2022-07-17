package top.imzdx.qqpush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.imzdx.qqpush.model.po.User;

import java.util.Optional;

/**
 * @author Renxing
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {
    @Query("select u from User u where u.cipher = ?1")
    Optional<User> findByCipher(String cipher);

    @Query(value = "SELECT COUNT(1) FROM message_log WHERE uid=?1 AND DATE_FORMAT( time, '%Y%m%d' ) = DATE_FORMAT( CURDATE() , '%Y%m%d' )", nativeQuery = true)
    Integer getTodayUseCount(Long uid);

    @Query(value = "SELECT COUNT(1) FROM message_log WHERE uid=?1 AND time >= DATE_SUB(now(), interval 3 SECOND)", nativeQuery = true)
    Integer getLastThreeSecondUseCount(Long uid);

    Optional<User> findByName(String userName);

    Optional<User> findByOpenid(String openid);

    User findByTelegramId(Long telegramId);
}
