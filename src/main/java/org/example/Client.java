package org.example;

import com.google.gson.Gson;

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
    private Reward reward;
    private final String cookie;
    private final static String AUTHORITY = "tanki.su";
    private final static String ACCEPT = "application/json,text/javascript,*/*;q=0.01";
    private final static String ACCEPT_LANGUAGE = "ru,en;q=0.9";
    private final static String CONTENT_TYPE = "application/json";
    private final static String ORIGIN = "https://tanki.su";
    private final static String SEC_CH_UA = "\"Chromium\";v=\"118\", \"YaBrowser\";v=\"23.11\", \"Not=A?Brand\";v=\"99\", \"Yowser\";v=\"2.5\"";
    private final static String SEC_CH_UA_MOBILE = "?0";
    private final static String SEC_CH_UA_PLATFORM = "\"Windows\"";
    private final static String SEC_FETCH_DEST = "empty";
    private final static String SEC_FETCH_MODE = "cors";
    private final static String SEC_FETCH_SITE = "same-origin";
    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 YaBrowser/23.11.0.0 Safari/537.36";

    public Client(final String nameCookieFile) {
        try {
            this.cookie = Files.readString(Path.of(nameCookieFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getDailyReward() {
        if (!reward.data().items().isEmpty()) {
            String item = reward.data().items().getFirst().product_code();
            String requestBody = "{\"product_code\":\"" +
                    item +
                    "\",\"language\":\"ru\",\"transaction_id\":\"" +
                    UUID.randomUUID() +
                    "\",\"expected_prices\":[{\"code\":\"gold\",\"amount\":\"0\",\"item_type\":\"currency\"}]}";

            String uri = "https://tanki.su/wotup/claim_product/purchase_product_vc/";
            String referer = "https://tanki.su/ru/daily-check-in/?utm_campaign=wot-wgcc&utm_medium=link&utm_source=global-nav";
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = getRequestWithHeaders()
                    .setHeader("Referer", referer)
                    .uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
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
                .thenAccept(this::getProductCode)
                .join();
    }

    private HttpRequest.Builder getRequestWithHeaders() {
        return HttpRequest.newBuilder()
                .setHeader("Authority", AUTHORITY)
                .setHeader("Accept", ACCEPT)
                .setHeader("Accept-Language", ACCEPT_LANGUAGE)
                .setHeader("Content-Type", CONTENT_TYPE)
                .setHeader("Cookie", cookie)
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
        int currentYear = calendar.get(Calendar.YEAR) % 100;
        long currentUnixTime = System.currentTimeMillis() / 1000L;

        String[] shortMonths = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        String replacement = shortMonths[currentMonth] + currentYear;

        String requestPayload = "{\"product_codes\":" +
                "[\"claim_MONTH_1\"," +
                "\"claim_MONTH_2\"," +
                "\"claim_MONTH_3\"," +
                "\"claim_MONTH_4\"," +
                "\"claim_MONTH_5\"," +
                "\"claim_MONTH_6\"," +
                "\"claim_MONTH_7\"," +
                "\"claim_MONTH_8\"," +
                "\"claim_MONTH_9\"," +
                "\"claim_MONTH_10\"," +
                "\"claim_MONTH_11\"," +
                "\"claim_MONTH_12\"," +
                "\"claim_MONTH_13\"," +
                "\"claim_MONTH_14\"," +
                "\"claim_MONTH_15\"," +
                "\"claim_MONTH_16\"," +
                "\"claim_MONTH_17\"," +
                "\"claim_MONTH_18\"," +
                "\"claim_MONTH_19\"," +
                "\"claim_MONTH_20\"," +
                "\"claim_MONTH_21\"," +
                "\"claim_MONTH_22\"," +
                "\"claim_MONTH_23\"," +
                "\"claim_MONTH_24\"," +
                "\"claim_MONTH_25\"," +
                "\"claim_MONTH_26\"," +
                "\"claim_MONTH_27\"," +
                "\"claim_MONTH_28\"," +
                "\"claim_MONTH_29\"," +
                "\"claim_MONTH_30\"," +
                "\"claim_MONTH_31\"]," +
                "\"language\":\"ru\",\"etag\":UNIXTIME}";
        requestPayload = requestPayload.replace("MONTH", replacement);
        requestPayload = requestPayload.replace("UNIXTIME", String.valueOf(currentUnixTime));

        return requestPayload;
    }

    private void getProductCode(String responseBody) {
        this.reward = new Gson().fromJson(responseBody, Reward.class);
    }
}
