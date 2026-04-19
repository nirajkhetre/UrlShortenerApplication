package in.avaloneco.UrlShortenerApplication.algoritham;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShortCodeGenerator {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int CODE_LENGTH = 7;

    public static String generate(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return base62Encode(hashBytes);
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("SHA-256 algorithm not available",e);
        }
    }

    public static String base62Encode(byte[] hashByte){
        BigInteger value = new BigInteger(1,hashByte);
        BigInteger base = BigInteger.valueOf(62);

        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i =0;i< CODE_LENGTH;i++){
            BigInteger[] divMod = value.divideAndRemainder(base);
            sb.append(BASE62.charAt(divMod[1].intValue()));
            value = divMod[0];
        }
        return sb.toString();
    }
}
