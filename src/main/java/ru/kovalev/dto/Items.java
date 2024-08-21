package ru.kovalev.dto;

import com.google.gson.annotations.SerializedName;

public record Items(
        @SerializedName("product_code")
        String productCode
) {
}
