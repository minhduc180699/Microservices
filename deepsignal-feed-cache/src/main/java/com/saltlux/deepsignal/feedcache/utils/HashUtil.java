package com.saltlux.deepsignal.feedcache.utils;

import org.apache.commons.codec.digest.MurmurHash3;

import java.nio.charset.StandardCharsets;

public class HashUtil {
    public static String hash128(String content){
        long[] result = MurmurHash3.hash128(content.getBytes(StandardCharsets.UTF_8));
        return String.valueOf(result[0]) + String.valueOf(result[1]);
    }
}
