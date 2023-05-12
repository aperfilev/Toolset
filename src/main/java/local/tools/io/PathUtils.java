package local.tools.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import local.tools.logs.Logger;
import local.tools.utils.StringUtils;

public final class PathUtils {

    private PathUtils() {}

    public static String composeQuery(Map<String,String> mapQueryParameters) {
        if ((mapQueryParameters == null) || (mapQueryParameters.isEmpty()))
            return "";

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String,String> entry : mapQueryParameters.entrySet()) {
            if ((entry == null) || StringUtils.isNullOrEmpty(entry.getKey()) || (entry.getValue() == null))
                continue;

            if (i > 0) sb.append("&");
            
            sb.append(entry.getKey().trim());
            sb.append("=");
            sb.append(entry.getValue());

            ++i;
        }
        return sb.toString();
    }

    public static String getHostFromURL(String urlPath) throws MalformedURLException {
        URL url = new URL(urlPath);
        return url.getHost();
    }

    public static boolean areSameGlobaly(String path0, String path1) {
        java.io.File path0File = new java.io.File(path0);
        java.io.File path1File = new java.io.File(path1);

        try {
            String canPath0 = path0File.getCanonicalPath();
            String canPath1 = path1File.getCanonicalPath();
            return canPath0.equalsIgnoreCase(canPath1);
        } catch(IOException e) {
            return false;
        }
    }

    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException ignored) {
        }
        return url;
    }
    
    public static String encodeURLTail(String url) {
        String[] parts = url.split("\\?", 2);
        if (parts.length == 2) {
            try {
                parts[1] = URLEncoder.encode(parts[1], StandardCharsets.UTF_8.displayName());
            } catch (UnsupportedEncodingException ignored) {}
            return parts[0] + "?" + parts[1];
        }
        return url;
        //return url.replaceAll(" ", "%20");
    }
    
    public static String removeSpecialSymbols(String line) {
        line = line.replaceAll("[,//:\\*<>\"|\\?\\\\]+", "");
        line = line.replaceAll("[\\. ]+$", "");
        return line;
    }
    
    public static String combinePath(String host_url, String internal_url) {
        if(internal_url.startsWith("http://") || internal_url.startsWith("https://")) {
            return internal_url;
        } else {
            if (host_url.endsWith("/")) {
                try {
                    host_url = host_url.substring(0, host_url.length()-1);
                } catch(StringIndexOutOfBoundsException ignored) {
                }
            }
            if (internal_url.startsWith("/")) {
                try {
                    internal_url = internal_url.substring(1);
                } catch(StringIndexOutOfBoundsException ignored) {
                }
            }
            return host_url + '/' + internal_url;
        }
    }
}
