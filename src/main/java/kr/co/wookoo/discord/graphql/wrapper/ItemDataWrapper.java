package kr.co.wookoo.discord.graphql.wrapper;

import kr.co.wookoo.discord.graphql.dto.GraphQLItemDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemDataWrapper {
    private List<GraphQLItemDto> items;
}
