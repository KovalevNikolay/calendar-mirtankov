package ru.kovalev.util;

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
    public static final String[] MONTH_SHORT_NAME = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
    public static final String TEMPLATE_BODY = """
            {"product_codes":[
            "claim_MONTH_1",
            "claim_MONTH_2",
            "claim_MONTH_3",
            "claim_MONTH_4",
            "claim_MONTH_5",
            "claim_MONTH_6",
            "claim_MONTH_7",
            "claim_MONTH_8",
            "claim_MONTH_9",
            "claim_MONTH_10",
            "claim_MONTH_11",
            "claim_MONTH_12",
            "claim_MONTH_13",
            "claim_MONTH_14",
            "claim_MONTH_15",
            "claim_MONTH_16",
            "claim_MONTH_17",
            "claim_MONTH_18",
            "claim_MONTH_19",
            "claim_MONTH_20",
            "claim_MONTH_21",
            "claim_MONTH_22",
            "claim_MONTH_23",
            "claim_MONTH_24",
            "claim_MONTH_25",
            "claim_MONTH_26",
            "claim_MONTH_27",
            "claim_MONTH_28",
            "claim_MONTH_29",
            "claim_MONTH_30",
            "claim_MONTH_31"],
            "language":"ru","etag":UNIXTIME}
            """;

    private ServiceUtil() {
    }
}
