package local.tools.serial.json.discovery;

final class JSONTokener {

    private int position;
    private final String source;

    JSONTokener(final String source) {
        this.position = 0;
        this.source = source;
    }

    void back() {
        if (this.position > 0) {
            this.position -= 1;
        }
    }

    boolean hasMore() {
        return this.position < this.source.length();
    }

    boolean isEnd() {
        return !hasMore();
    }

    char next() {
        if (hasMore()) {
            char c = this.source.charAt(this.position);
            this.position += 1;
            return c;
        }
        return 0;
    }

    char next(char c) throws JSONException {
        char n = next();
        if (n != c) {
            throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
        }
        return n;
    }

    String next(int n) throws JSONException {
        int i = this.position;
        int j = i + n;
        if (j >= this.source.length()) {
            throw syntaxError("Substring bounds error");
        }
        this.position += n;
        return this.source.substring(i, j);
    }

    char nextClean() throws JSONException {
        while (true) {
            char c = next();
            //<editor-fold defaultstate="collapsed" desc="Removes C-style comments">
            if (c == '/') {
                switch (next()) {
                    //<editor-fold defaultstate="collapsed" desc="Single-line comment removing">
                    case '/':
                        do {
                            c = next();
                        } while (c != '\n' && c != '\r' && c != 0);
                        break;
                        //</editor-fold>
                        //<editor-fold defaultstate="collapsed" desc="Multi-line comment removing">
                    case '*':
                        for (;;) {
                            c = next();
                            if (c == 0) {
                                throw syntaxError("Unclosed comment");
                            }
                            if (c == '*') {
                                if (next() == '/') {
                                    break;
                                }
                                back();
                            }
                        }
                        break;
                        //</editor-fold>
                    default:
                        back();
                        return '/';
                }
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Removes #-dash comments">
            else if (c == '#') {
                do {
                    c = next();
                } while (c != '\n' && c != '\r' && c != 0);
            } 
            //</editor-fold>
            else if (c == 0 || c > ' ') {
                return c;
            }
        }
    }

    String nextString(char quote) throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            switch (c) {
                case 0:
                case '\n':
                case '\r':
                    throw syntaxError("Unterminated string");
                case '\\':
                    c = next();
                    switch (c) {
                        case 'b':
                            sb.append('\b');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 'u':
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        case 'x':
                            sb.append((char) Integer.parseInt(next(2), 16));
                            break;
                        default:
                            sb.append(c);
                    }
                    break;
                default:
                    if (c == quote) {
                        return sb.toString();
                    }
                    sb.append(c);
            }
        }
    }

    String nextTo(char d) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            if (c == d || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    String nextTo(String delimiters) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            if (delimiters.indexOf(c) >= 0 || c == 0 ||
                    c == '\n' || c == '\r') {
                if (c != 0) {
                    back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    Object nextValue() throws JSONException {
        char c = nextClean();
        switch (c) {
            case '"':
            case '\'':
                return nextString(c);
            case '{':
                back();
                return new JSONObject(this);
            case '[':
                back();
                return new JSONArray(this);
        }

        /*
         * Handle unquoted text. This could be the values true, false, or
         * null, or it can be a number. An implementation (such as this one)
         * is allowed to also accept non-standard forms.
         *
         * Accumulate characters until we reach the isEnd of the text or a
         * formatting character.
         */
        StringBuilder sb = new StringBuilder();
        char b = c;
        while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) { // e.i c not in charlist(",:]}/\\\"[{;=#")
            sb.append(c);
            c = next();
        }
        back();

        /*
         * If it is true, false, or null, return the proper value.
         */
        String s = sb.toString().trim();
        if (s.isEmpty()) {
            throw syntaxError("Missing value");
        }
        if (s.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (s.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (s.equalsIgnoreCase("null")) {
            return null;
        }

        /*
         * If it might be a number, try converting it. We support the 0- and 0x-
         * conventions. If a number cannot be produced, then the value will just
         * be a string. Note that the 0-, 0x-, plus, and implied string
         * conventions are non-standard. A JSON parser is free to accept
         * non-JSON forms as long as it accepts all correct JSON forms.
         */

        if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
            if (b == '0') {
                if (s.length() > 2 &&
                        (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
                    try {
                        return Integer.parseInt(s.substring(2), 16);
                    } catch (Exception e) {
                        /* Ignore the error */
                    }
                } else {
                    try {
                        return Integer.parseInt(s, 8);
                    } catch (Exception e) {
                        /* Ignore the error */
                    }
                }
            }
            try {
                return Integer.valueOf(s);
            } catch (Exception e) {
                try {
                    return Long.valueOf(s);
                } catch (Exception f) {
                    try {
                        return Double.valueOf(s);
                    } catch (Exception g) {
                        return s;
                    }
                }
            }
        }
        return s;
    }

    char skipTo(char to) {
        char c;
        int index = this.position;
        do {
            c = next();
            if (c == 0) {
                this.position = index;
                return c;
            }
        } while (c != to);
        back();
        return c;
    }

    boolean skipPast(String to) {
        this.position = this.source.indexOf(to, this.position);
        if (this.position < 0) {
            this.position = this.source.length();
            return false;
        }
        this.position += to.length();
        return true;

    }

    JSONException syntaxError(String message) {
        return new JSONException(message);
    }

    @Override
    public String toString() {
        return String.format(" at character %d of %s", this.position, this.source);
    }
}