package org.example;

import java.io.IOException;

public class CalendarMirTankovApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        client.findAnAvailableReward();
        client.getDailyReward();
    }
}