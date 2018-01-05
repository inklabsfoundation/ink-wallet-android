package ink.qtum.org.utils;

/**
 * Created by SV on 05.01.2018.
 */

public class TextUtils {

    public static String txHashToShort(String txHash) {
        String begin = txHash.substring(0, 7).toUpperCase();
        String end = txHash.substring(txHash.length() - 7, txHash.length()).toUpperCase();
        return begin + "..." + end;
    }
}
