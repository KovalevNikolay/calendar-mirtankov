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
import java.util.Random;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static String productCodeAvailable;
    private static Settings settings;
    public static void main(String[] args) throws IOException, InterruptedException {
        initializeBrowser();
        findAnAvailableReward();
        getDailyReward();
    }
    private static void getDailyReward() throws IOException, InterruptedException {

        StringBuilder requestBody = new StringBuilder();
        requestBody
                .append("{\"product_code\":\"")
                .append(productCodeAvailable)
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
        productCodeAvailable = gson.fromJson(responseBody, Reward.class).data().items().getFirst().product_code();
    }
}