package ru.kovalev;

import ru.kovalev.service.Service;
import java.net.http.HttpClient;


public class CalendarMirTankovApp {
    public static void main(String[] args) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String cookieFile = "cookie.txt";
            Service service = new Service(client, cookieFile);
            if (service.checkReward()) {
                service.takeReward();
            }
        }
    }
}