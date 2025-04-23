package kr.co.wookoo.discord.graphql.dto;

import kr.co.wookoo.discord.dto.ItemDto;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GraphQLItemDto {
    private String id;
    private String name;
    private String wikiLink;
}