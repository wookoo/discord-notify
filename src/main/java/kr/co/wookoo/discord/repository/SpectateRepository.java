package kr.co.wookoo.discord.repository;

import kr.co.wookoo.discord.entity.Item;
import kr.co.wookoo.discord.entity.Spectate;
import kr.co.wookoo.discord.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpectateRepository extends JpaRepository<Spectate, Long> {
    Optional<Spectate> findByUser(User user);
}
