package org.example;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class Main {
    private static Reward reward;
    private static Settings settings;
    public static void main(String[] args) throws IOException, InterruptedException {
        initializeBrowser();
        findAnAvailableReward();
        getDailyReward();
    }
    private static void getDailyReward() throws IOException, InterruptedException {
        if (!reward.data().items().isEmpty()) {
            String item = reward.data().items().getFirst().product_code();
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

            HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authority", settings.authority())
                    .setHeader("Accept", settings.accept())
                    .setHeader("Accept-Language", settings.acceptLanguage())
                    .setHeader("Content-Type", settings.contentType())
                    .setHeader("Cookie", settings.cookie())
                    .setHeader("Origin", settings.origin())
                    .setHeader("Referer", referer)
                    .setHeader("Sec-Ch-Ua", settings.secChUa())
                    .setHeader("Sec-Ch-Ua-Mobile", settings.secChUaMobile())
                    .setHeader("Sec-Ch-Ua-Platform", settings.secChUaPlatform())
                    .setHeader("Sec-Fetch-Dest", settings.secFetchDest())
                    .setHeader("Sec-Fetch-Mode", settings.secFetchMode())
                    .setHeader("Sec-Fetch-Site", settings.secFetchSite())
                    .setHeader("User-Agent", settings.userAgent())
                    .uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        else {
            System.out.println("Доступных наград нет!");
        }
    }
    private static void findAnAvailableReward() throws IOException, InterruptedException {

        String uri = "https://tanki.su/wotup/claim_product/get_products_list/";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("Authority", settings.authority())
                .setHeader("Accept", settings.accept())
                .setHeader("Accept-Language", settings.acceptLanguage())
                .setHeader("Content-Type", settings.contentType())
                .setHeader("Cookie", settings.cookie())
                .setHeader("Origin", settings.origin())
                .setHeader("Referer", settings.referer())
                .setHeader("Sec-Ch-Ua", settings.secChUa())
                .setHeader("Sec-Ch-Ua-Mobile", settings.secChUaMobile())
                .setHeader("Sec-Ch-Ua-Platform", settings.secChUaPlatform())
                .setHeader("Sec-Fetch-Dest", settings.secFetchDest())
                .setHeader("Sec-Fetch-Mode", settings.secFetchMode())
                .setHeader("Sec-Fetch-Site", settings.secFetchSite())
                .setHeader("User-Agent", settings.userAgent())
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofFile(new File("calendar.json").toPath()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(Main::getProductCode)
                .join();
    }
    private static void initializeBrowser() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("settings.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        settings = gson.fromJson(reader, Settings.class);
    }
    private static void getProductCode(String responseBody) {
        Gson gson = new Gson();
        reward = gson.fromJson(responseBody, Reward.class);
    }
}