package kr.co.wookoo.discord.service;

import kr.co.wookoo.discord.dto.ItemDto;
import kr.co.wookoo.discord.entity.Item;
import kr.co.wookoo.discord.graphql.dto.GraphQLItemDto;
import kr.co.wookoo.discord.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //플리마켓 전용 검색
    public List<ItemDto> findByName(String name) {

        List<Item> itemList = itemRepository.findByKeyword(name, PageRequest.of(0,25));
        return itemList.stream().map(ItemDto::from).toList();

    }

    public void saveItemsFromGraphQL(List<GraphQLItemDto> koreanList, List<GraphQLItemDto> englishList) {
        for (GraphQLItemDto koItem : koreanList) {
            String id = koItem.getId();
            Item item = itemRepository.findByTarkovDevfk(id).orElse(null);

            if (item == null) {
                item = Item.builder().korean(koItem.getName()).fleaMarketSellable(true).wikiLink(koItem.getWikiLink()).tarkovDevfk(id).build();
            } else {
                item.setKorean(koItem.getName()); // 한글만 업데이트
            }
            itemRepository.save(item);
        }
        for (GraphQLItemDto enItem : englishList) {
            itemRepository.findByTarkovDevfk(enItem.getId()).ifPresent(item -> {
                item.setEnglish(enItem.getName()); // 영어만 업데이트
                itemRepository.save(item);
            });
        }
    }

}
