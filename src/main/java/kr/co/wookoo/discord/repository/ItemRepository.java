package kr.co.wookoo.discord.repository;

import kr.co.wookoo.discord.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByTarkovDevfk(String fk);

    @Query("""
    SELECT i FROM Item i
    WHERE i.fleaMarketSellable = true
      AND (
        LOWER(i.english) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(i.korean) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
""")
    List<Item> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
