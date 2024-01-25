package org.example;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public record Reward(
        @SerializedName("data")
        Data data
) {}

record Data(
        @SerializedName("items")
        ArrayList<Items> items
) {}

record Items(
        @SerializedName("product_code")
        String product_code
) {}
