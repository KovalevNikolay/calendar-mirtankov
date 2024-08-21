package ru.kovalev.dto;

import com.google.gson.annotations.SerializedName;

public record Reward(
        @SerializedName("data")
        Data data
) {
}
