package kr.co.wookoo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GraphQLClient {

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

            return allTraders.stream()
                    .filter(trader -> !excludedNames.contains(trader.getName().toLowerCase()))
                    .toList();

        }
    }
}
