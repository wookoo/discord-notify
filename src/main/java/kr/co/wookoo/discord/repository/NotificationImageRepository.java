package kr.co.wookoo.discord.repository;

import kr.co.wookoo.discord.entity.NotificationImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationImageRepository extends JpaRepository<NotificationImage, Long> {
}
