package info.ernestas.awesomecoin.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {

    public static String calculateSha256Hash(String data) {
        return DigestUtils.sha256Hex(data);
    }

}
