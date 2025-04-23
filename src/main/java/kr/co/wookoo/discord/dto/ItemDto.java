package kr.co.wookoo.discord.dto;

import kr.co.wookoo.discord.entity.Item;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemDto {
    private final Long id;
    private final String tarkovDevfk;
    private final String korean;
    private final String english;
    public static ItemDto from(Item item) {
        return new ItemDto(item.getId(), item.getTarkovDevfk(), item.getKorean(), item.getEnglish());
    }

}
