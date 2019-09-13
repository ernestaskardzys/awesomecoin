package info.ernestas.awesomecoin.util;

public class HashTargetUtil {

    public static String getHashTarget(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

}
