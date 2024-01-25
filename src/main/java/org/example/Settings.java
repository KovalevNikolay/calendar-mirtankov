package org.example;

import com.google.gson.annotations.SerializedName;

public record Settings(
        @SerializedName("Authority")
        String authority,
        @SerializedName("Accept")
        String accept,
        @SerializedName("Accept-Language")
        String acceptLanguage,
        @SerializedName("Content-Type")
        String contentType,
        @SerializedName("Cookie")
        String cookie,
        @SerializedName("Origin")
        String origin,
        @SerializedName("Referer")
        String referer,
        @SerializedName("Sec-Ch-Ua")
        String secChUa,
        @SerializedName("Sec-Ch-Ua-Mobile")
        String secChUaMobile,
        @SerializedName("Sec-Ch-Ua-Platform")
        String secChUaPlatform,
        @SerializedName("Sec-Fetch-Dest")
        String secFetchDest,
        @SerializedName("Sec-Fetch-Mode")
        String secFetchMode,
        @SerializedName("Sec-Fetch-Site")
        String secFetchSite,
        @SerializedName("User-Agent")
        String userAgent
) { }
