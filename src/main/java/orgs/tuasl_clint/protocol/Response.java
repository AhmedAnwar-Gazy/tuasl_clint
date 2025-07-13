// src/orgs/protocol/Response.java
package orgs.tuasl_clint.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import orgs.tuasl_clint.utils.LocalDateTimeAdapter;
import orgs.tuasl_clint.utils.TimestampAdapter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Response {
    private boolean success;
    private String message;
    private String data; // Stores the JSON string representation of the response data object

    // Use a static Gson instance for consistent serialization
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    public Response(boolean success, String message, String data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    // Method to convert the Response object to a JSON string for sending over the network
    public String toJson() {
        return gson.toJson(this);
    }

    // For debugging/logging
    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}