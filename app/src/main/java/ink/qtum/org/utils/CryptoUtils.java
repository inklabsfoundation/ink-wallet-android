package ink.qtum.org.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Created by SV on 06.01.2018.
 */

public class CryptoUtils {

    public static String encodeBase64(String text) {
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static String decodeBase64(String base64) {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);
    }
}
