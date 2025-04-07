package kr.co.wookoo.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.wookoo.TraderResetTime;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClient implements Client {

    private static final String URL = "https://tarkovbot.eu/tools/pve/trader-resets/gettimes";
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public HttpClient() {
        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }


    public List<TraderResetTime> fetchTraderResetTimes() throws IOException {
        Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();


        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String text = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            Instant now = Instant.now();
            List<TraderResetTime> traderResetTimes = mapper.readerForListOf(TraderResetTime.class).readValue(text);
            return traderResetTimes.stream()
                    .filter(t -> t.getResetTime().isAfter(now))
                    .collect(Collectors.toList());
        }

    }
}
