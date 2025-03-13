package io.robothouse.urlshortener.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RedisHash("Url")
public record Url(@Id String key, String longUrl, String shortUrl) {

    public static String createKey(String longUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash).substring(0, 12);
        } catch (NoSuchAlgorithmException e) {
            // Algorithm is guaranteed to be available (hardcoded)
            return null;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
