package kr.co.wookoo.discord.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

public class ItemPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public int price;
    private Instant time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
