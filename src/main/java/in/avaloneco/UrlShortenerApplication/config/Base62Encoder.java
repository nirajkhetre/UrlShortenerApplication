package in.avaloneco.UrlShortenerApplication.config;

public class Base62Encoder {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long val){
        if (val == 0) return "0";           // ← BEFORE the loop
        StringBuilder sb = new StringBuilder();
        while (val > 0){
            sb.append(BASE62.charAt((int) (val % 62)));
            val /= 62;
        }
        return sb.reverse().toString();
    }


    public static long decode(String str){
        long result =0;
        for (int i = 0;i<str.length();i++){
            result = result * 62 +BASE62.indexOf(str.charAt(i));
        }
        return result;
    }
}
