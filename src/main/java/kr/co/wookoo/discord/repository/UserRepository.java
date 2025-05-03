package kr.co.wookoo.discord.repository;

import kr.co.wookoo.discord.entity.Spectate;
import kr.co.wookoo.discord.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMemberId(Long id);
}
