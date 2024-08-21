package ru.kovalev.service;

import com.google.gson.Gson;
import ru.kovalev.dto.Reward;
import ru.kovalev.util.ServiceUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

public class Service {

    private Reward reward;
    private String cookie;
    private final HttpClient httpClient;

    public Service(HttpClient httpClient, String cookieFile) {
        this.httpClient = httpClient;
        this.cookie = loadCookie(cookieFile);
    }

    private String loadCookie(String cookieFile) {
        try {
            return Files.readString(Path.of(cookieFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void takeReward() {
        String rewardCode = reward.data().items().getFirst().productCode();
        String requestBody = "{\"product_code\":\"%s\",\"language\":\"ru\",\"transaction_id\":\"%s\",\"expected_prices\":[{\"code\":\"gold\",\"amount\":\"0\",\"item_type\":\"currency\"}]}"
                .formatted(rewardCode, UUID.randomUUID());

        String uri = "https://tanki.su/wotup/claim_product/purchase_product_vc/";
        String referer = "https://tanki.su/ru/daily-check-in/?utm_campaign=wot-wgcc&utm_medium=link&utm_source=global-nav";

        HttpRequest request = createRequest(uri, referer, requestBody);

        try {
            httpClient.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean checkReward() {
        String requestBody = generateBody();
        String uri = "https://tanki.su/wotup/claim_product/get_products_list/";
        String referer = "https://tanki.su/ru/daily-check-in/?utm_source=global-nav&utm_medium=link&utm_campaign=wot-wgcc";

        HttpRequest request = createRequest(uri, referer, requestBody);

        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            getProductCode(response.body());
            return reward.data() != null && !reward.data().items().isEmpty();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpRequest createRequest(String uri, String referer, String requestBody) {
        return HttpRequest.newBuilder(URI.create(uri))
                .headers(
                        "authority", ServiceUtil.AUTHORITY,
                        "accept", ServiceUtil.ACCEPT,
                        "accept-language", ServiceUtil.ACCEPT_LANGUAGE,
                        "content-type", ServiceUtil.CONTENT_TYPE,
                        "cookie", cookie,
                        "origin", ServiceUtil.ORIGIN,
                        "sec-ch-ua", ServiceUtil.SEC_CH_UA,
                        "sec-ch-ua-mobile", ServiceUtil.SEC_CH_UA_MOBILE,
                        "sec-ch-ua-platform", ServiceUtil.SEC_CH_UA_PLATFORM,
                        "sec-fetch-dest", ServiceUtil.SEC_FETCH_DEST,
                        "sec-fetch-mode", ServiceUtil.SEC_FETCH_MODE,
                        "sec-fetch-site", ServiceUtil.SEC_FETCH_SITE,
                        "user-agent", ServiceUtil.USER_AGENT,
                        "referer", referer
                )
                .POST(BodyPublishers.ofString(requestBody))
                .build();
    }

    private String generateBody() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue() - 1;
        int currentYear = now.getYear() % 100;
        long currentUnixTime = System.currentTimeMillis() / 1000L;

        String replacement = ServiceUtil.MONTH_SHORT_NAME[currentMonth] + currentYear;
        String body = ServiceUtil.TEMPLATE_BODY;

        return body.replace("MONTH", replacement).replace("UNIXTIME", String.valueOf(currentUnixTime));
    }

    private void getProductCode(String responseBody) {
        this.reward = new Gson().fromJson(responseBody, Reward.class);
    }
}
