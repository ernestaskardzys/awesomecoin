package info.ernestas.awesomecoin.util;

import java.security.Key;
import java.util.Base64;

public class StringUtil {

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
