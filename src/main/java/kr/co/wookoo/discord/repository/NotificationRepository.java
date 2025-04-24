package kr.co.wookoo.discord.repository;

import kr.co.wookoo.discord.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select * from notification order by rand() limit 1", nativeQuery = true)
    Optional<Notification> getRandom();
}
