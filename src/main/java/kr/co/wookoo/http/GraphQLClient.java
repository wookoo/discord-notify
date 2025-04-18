package kr.co.wookoo.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.wookoo.dto.ItemInfo;
import kr.co.wookoo.dto.ItemPrice;
import kr.co.wookoo.dto.TraderResetTime;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GraphQLClient implements Client {

    private static final String ENDPOINT = "https://api.tarkov.dev/graphql";
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public GraphQLClient() {

        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    public List<TraderResetTime> fetchTraderResetTimes() throws IOException {
        String query = """
                    query {
                      traderResetTimes {
                        name
                        resetTimestamp
                      }
                    }
                """;

        String jsonBody = mapper.writeValueAsString(new GraphQLRequest(query));

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .build();


        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String text = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            JsonNode root = mapper.readTree(text);
            JsonNode tradersNode = root.get("data").get("traderResetTimes");
            List<TraderResetTime> allTraders =
                    mapper.readerForListOf(TraderResetTime.class).readValue(tradersNode.toString());


            Set<String> excludedNames = Set.of("fence", "lightkeeper", "ref", "btr driver");
            Instant now = Instant.now();
            return allTraders.stream()
                    .filter(trader -> !excludedNames.contains(trader.getName().toLowerCase()) && trader.getResetTime().isAfter(now))
                    .toList();
        }
    }

    public List<ItemPrice> fetchItemFleaMarketPriceList(String id) throws IOException {
        String query = String.format("""
                query {
                  historicalItemPrices(id: "%s", gameMode: pve) {
                    price
                    timestamp
                  }
                }
                """, id);

        String body = mapper.writeValueAsString(new GraphQLRequest(query));

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(RequestBody.create(body, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String text = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            JsonNode root = mapper.readTree(text).get("data").get("historicalItemPrices");
            return mapper.readerForListOf(ItemPrice.class).readValue(root);
        }
    }

    public int getBitcoinPrice() throws IOException {
        String coinId = "59faff1d86f7746c51718c9c";

        String query = String.format("""
                query {
                  item(id: "%s") {
                    sellFor {
                      price
                    }
                  }
                }
                """, coinId);

        String body = mapper.writeValueAsString(new GraphQLRequest(query));

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(RequestBody.create(body, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String text = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            JsonNode root = mapper.readTree(text).get("data").get("item").get("sellFor");
            List<Price> priceList = mapper.readerForListOf(Price.class).readValue(root);
            Price maxPrice = priceList.stream()
                    .max(Comparator.comparingInt(Price::getPrice)).orElse(null);
            if (maxPrice == null) {
                return 0;
            }
            return maxPrice.getPrice();
        }
    }


    private static class Price {
        private int price;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
