package orgs.tuasl_clint.models2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.cj.x.protobuf.MysqlxExpr;
import orgs.tuasl_clint.protocol.Response;
import orgs.tuasl_clint.utils.LocalDateTimeAdapter;

import java.security.InvalidParameterException;
import java.security.Timestamp;
import java.time.LocalDateTime;

public class packet {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,new LocalDateTimeAdapter())
            .registerTypeAdapter(Timestamp.class,new LocalDateTimeAdapter())
            .serializeNulls()
            .create();
    public packet( Response res)throws InvalidParameterException{

    }
}
