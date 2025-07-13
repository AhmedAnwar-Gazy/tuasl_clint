package orgs.tuasl_clint.utils;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        return new JsonPrimitive(src.toLocalDateTime().format(FORMATTER));
    }

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String dateStr = json.getAsString();
        // Truncate to 9 decimal digits if longer
        if (dateStr.length() > 29) { // 29 = length of "2025-07-10T22:49:01.890683300"
            dateStr = dateStr.substring(0, 29);
        }
        LocalDateTime ldt = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return Timestamp.valueOf(ldt);
    }
}