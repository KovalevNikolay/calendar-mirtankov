package org.example;

public class CalendarMirTankovApp {
    public static void main(String[] args) {
        Client client = new Client("cookie.txt");
        client.findAnAvailableReward();
        client.getDailyReward();
    }
}