package local.tools.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public final class StringUtils {

    public static boolean isEmpty(String value) {
        return (value == null || value.isEmpty());
    }
    
    public static boolean isNotEmpty(String value) {
        return !(value == null || value.isEmpty());
    }

    public static boolean isNullOrEmpty(String value) {
        return (value == null || value.isEmpty());
    }

    public static List<String> split(String line, String separator, boolean emptyEntries) {
        List<String> lines = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(line, separator);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if(!token.isEmpty() || emptyEntries)
                lines.add(token);
        }
        return lines;
    }

    public static String join(Iterable<String> s) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = s.iterator();
        if (iter.hasNext()) {
            sb.append(iter.next());
            while (iter.hasNext()) {
                sb.append(iter.next());
            }
        }
        return sb.toString();
    }

    public static String join(Iterable<String> s, String delimiter) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = s.iterator();
        if (iter.hasNext()) {
            sb.append(iter.next());
            while (iter.hasNext()) {
                sb.append(delimiter);
                sb.append(iter.next());
            }
        }
        return sb.toString();
    }
    
    public static String join(String... s) {
        return join(Arrays.asList(s));
    }

    public static String join(String[] s, String delimeter) {
        return join(Arrays.asList(s), delimeter);
    }

    public static List<String> wrapElements(List<String> elements, String prefix, String postfix, boolean lowerCase) {
        List<String> result = null;
        if(elements != null) {
            result = new ArrayList<>();
            for (String element : elements) {
                StringBuilder sb = new StringBuilder();
                sb.append(prefix);
                if ((element == null) || (element.length() == 0))
                    continue;
                if (lowerCase)
                    element = element.toLowerCase();
                sb.append(element);
                sb.append(postfix);
                result.add(sb.toString());
            }
        }
        return result;
    }

    public static List<String> wrapElements(List<String> elements, String bracket, boolean lowerCase) {
        return wrapElements(elements, bracket, bracket, lowerCase);
    }

    public static List<String> wrapElements(List<String> elements, String bracket) {
        return wrapElements(elements, bracket, bracket, false);
    }
}
