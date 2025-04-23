package kr.co.wookoo.discord.initializer;


import kr.co.wookoo.discord.graphql.GraphQLClient;
import kr.co.wookoo.discord.graphql.dto.GraphQLItemDto;
import kr.co.wookoo.discord.repository.ItemRepository;
import kr.co.wookoo.discord.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemInitializer  implements CommandLineRunner {

    private final ItemService itemService;
    private final GraphQLClient graphQLClient;
    @Override
    public void run(String... args) throws Exception {
        List<GraphQLItemDto> koreanList = graphQLClient.fetchItemList("ko");
        List<GraphQLItemDto> EnglishList = graphQLClient.fetchItemList("en");
        itemService.saveItemsFromGraphQL(koreanList,EnglishList);
    }
}
