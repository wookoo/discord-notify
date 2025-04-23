package kr.co.wookoo.discord.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    @Setter
    private String korean;
    @Setter
    private String english;
    private Boolean fleaMarketSellable;
    private String wikiLink;


    @Builder(access = AccessLevel.PUBLIC)
    private Item(String tarkovDevfk, String korean, String english, Boolean fleaMarketSellable, String wikiLink) {
        this.tarkovDevfk = tarkovDevfk;
        this.korean = korean;
        this.english = english;
        this.fleaMarketSellable = fleaMarketSellable;
        this.wikiLink = wikiLink;
    }

    public String getImageLink() {
        return "https://assets.tarkov.dev/" + this.tarkovDevfk + "-base-image.webp";
    }
}

