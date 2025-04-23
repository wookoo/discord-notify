package kr.co.wookoo.discord.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor

@Table(
        name = "item",
        indexes = {
                @Index(name = "idx_korean", columnList = "korean"),
                @Index(name = "idx_english", columnList = "english")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tarkovDevfk", columnNames = "tarkovDevfk")
        }
)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tarkovDevfk;
    private String korean;
    private String english;
    private Boolean fleaMarketSellable;
}

