package local.tools.serial.json.discovery;

public final class JSONException extends RuntimeException {
    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable t) {
        super(t.getMessage(), t);
    }
}
