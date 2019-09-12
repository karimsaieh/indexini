package tn.insat.pfe.searchservice.services.fallbacks.utils;

import org.springframework.data.domain.Pageable;

public class RedisUtils {

    private RedisUtils() {

    }

    public static String generateKey(String[] keyParts) {
        StringBuilder keySb = new StringBuilder();
        for (String part: keyParts) {
            keySb.append(part).append("&");
        }
        return keySb.toString();
    }
    public static String generateKey(String[] keyParts, Pageable pageable) {
        String key = generateKey(keyParts);
        key +=
                pageable.getPageNumber() + "&" +
                pageable.getPageSize() + "&" +
                pageable.getSort() + "&" +
                pageable.getOffset() + "&";

        return key;
    }
}
