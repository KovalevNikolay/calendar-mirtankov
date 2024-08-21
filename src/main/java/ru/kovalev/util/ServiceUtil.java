package ru.kovalev.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ServiceUtil {

    public static final String AUTHORITY = "tanki.su";
    public static final String ACCEPT = "application/json,text/javascript,*/*;q=0.01";
    public static final String ACCEPT_LANGUAGE = "ru,en;q=0.9";
    public static final String CONTENT_TYPE = "application/json";
    public static final String ORIGIN = "https://tanki.su";
    public static final String SEC_CH_UA = "\"Chromium\";v=\"118\", \"YaBrowser\";v=\"23.11\", \"Not=A?Brand\";v=\"99\", \"Yowser\";v=\"2.5\"";
    public static final String SEC_CH_UA_MOBILE = "?0";
    public static final String SEC_CH_UA_PLATFORM = "\"Windows\"";
    public static final String SEC_FETCH_DEST = "empty";
    public static final String SEC_FETCH_MODE = "cors";
    public static final String SEC_FETCH_SITE = "same-origin";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 YaBrowser/23.11.0.0 Safari/537.36";

    private ServiceUtil() {
    }
}
