package org.example;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.UUID;

public class Client {
    private static Reward REWARD;
    private final static String AUTHORITY = "tanki.su";
    private final static String ACCEPT = "application/json,text/javascript,*/*;q=0.01";
    private final static String ACCEPT_LANGUAGE = "ru,en;q=0.9";
    private final static String CONTENT_TYPE = "application/json";
    private final static String COOKIE;
    private final static String ORIGIN = "https://tanki.su";
    private final static String SEC_CH_UA = "\"Chromium\";v=\"118\", \"YaBrowser\";v=\"23.11\", \"Not=A?Brand\";v=\"99\", \"Yowser\";v=\"2.5\"";
    private final static String SEC_CH_UA_MOBILE = "?0";
    private final static String SEC_CH_UA_PLATFORM = "\"Windows\"";
    private final static String SEC_FETCH_DEST = "empty";
    private final static String SEC_FETCH_MODE = "cors";
    private final static String SEC_FETCH_SITE = "same-origin";
    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 YaBrowser/23.11.0.0 Safari/537.36";

    public void getDailyReward() throws IOException, InterruptedException {
        if (!REWARD.data().items().isEmpty()) {
            String item = REWARD.data().items().getFirst().product_code();
            StringBuilder requestBody = new StringBuilder();
            requestBody
                    .append("{\"product_code\":\"")
                    .append(item)
                    .append("\",\"language\":\"ru\",\"transaction_id\":\"")
                    .append(UUID.randomUUID())
                    .append("\",\"expected_prices\":[{\"code\":\"gold\",\"amount\":\"0\",\"item_type\":\"currency\"}]}");

            String uri = "https://tanki.su/wotup/claim_product/purchase_product_vc/";
            String referer = "https://tanki.su/ru/daily-check-in/?utm_campaign=wot-wgcc&utm_medium=link&utm_source=global-nav";
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = getRequestWithHeaders()
                    .setHeader("Referer", referer)
                    .uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } else {
            System.out.println("Доступных наград нет!");
        }
    }

    public void findAnAvailableReward() {

        String payload = toMakeListOfAwards();
        String uri = "https://tanki.su/wotup/claim_product/get_products_list/";
        String referer = "https://tanki.su/ru/daily-check-in/?utm_source=global-nav&utm_medium=link&utm_campaign=wot-wgcc";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = getRequestWithHeaders()
                .setHeader("Referer", referer)
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(Client::getProductCode)
                .join();
    }

    private HttpRequest.Builder getRequestWithHeaders() {
        return HttpRequest.newBuilder()
                .setHeader("Authority", AUTHORITY)
                .setHeader("Accept", ACCEPT)
                .setHeader("Accept-Language", ACCEPT_LANGUAGE)
                .setHeader("Content-Type", CONTENT_TYPE)
                .setHeader("Cookie", COOKIE)
                .setHeader("Origin", ORIGIN)
                .setHeader("Sec-Ch-Ua", SEC_CH_UA)
                .setHeader("Sec-Ch-Ua-Mobile", SEC_CH_UA_MOBILE)
                .setHeader("Sec-Ch-Ua-Platform", SEC_CH_UA_PLATFORM)
                .setHeader("Sec-Fetch-Dest", SEC_FETCH_DEST)
                .setHeader("Sec-Fetch-Mode", SEC_FETCH_MODE)
                .setHeader("Sec-Fetch-Site", SEC_FETCH_SITE)
                .setHeader("User-Agent", USER_AGENT);
    }

    private String toMakeListOfAwards() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR) % 100; //2024 -> 24
        long currentUnixTime = System.currentTimeMillis() / 1000L;

        String[] shortMonths = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        String replacement = shortMonths[currentMonth] + currentYear;

        String requestPayload = "{\"product_codes\":[\"claim_REPLACE_1\",\"claim_REPLACE_2\",\"claim_REPLACE_3\",\"claim_REPLACE_4\",\"claim_REPLACE_5\",\"claim_REPLACE_6\",\"claim_REPLACE_7\",\"claim_REPLACE_8\",\"claim_REPLACE_9\",\"claim_REPLACE_10\",\"claim_REPLACE_11\",\"claim_REPLACE_12\",\"claim_REPLACE_13\",\"claim_REPLACE_14\",\"claim_REPLACE_15\",\"claim_REPLACE_16\",\"claim_REPLACE_17\",\"claim_REPLACE_18\",\"claim_REPLACE_19\",\"claim_REPLACE_20\",\"claim_REPLACE_21\",\"claim_REPLACE_22\",\"claim_REPLACE_23\",\"claim_REPLACE_24\",\"claim_REPLACE_25\",\"claim_REPLACE_26\",\"claim_REPLACE_27\",\"claim_REPLACE_28\",\"claim_REPLACE_29\",\"claim_REPLACE_30\",\"claim_REPLACE_31\"],\"language\":\"ru\",\"etag\":UNIXTIME}";
        requestPayload = requestPayload.replace("REPLACE", replacement);
        requestPayload = requestPayload.replace("UNIXTIME", String.valueOf(currentUnixTime));

        return requestPayload;
    }

    static {
        try {
            COOKIE = Files.readString(Path.of("cookie.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getProductCode(String responseBody) {
        Gson gson = new Gson();
        REWARD = gson.fromJson(responseBody, Reward.class);
    }
}
