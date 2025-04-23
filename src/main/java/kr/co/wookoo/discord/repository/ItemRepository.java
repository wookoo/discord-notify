package kr.co.wookoo.discord.repository;

import kr.co.wookoo.discord.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByTarkovDevfk(String fk);
}
