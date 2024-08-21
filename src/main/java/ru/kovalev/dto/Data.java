package ru.kovalev.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record Data(
        @SerializedName("items")
        List<Items> items
) {
}
