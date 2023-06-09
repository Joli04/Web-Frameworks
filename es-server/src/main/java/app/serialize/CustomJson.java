package app.serialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
@JsonComponent
public class CustomJson {
    public static class Shallow{}
    public static class Summary extends Shallow{}

    public static class ShallowSerializer extends JsonSerializer<Object>{

        @Override
        public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper()
                    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL);

            mapper.registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            mapper.setConfig(mapper.getSerializationConfig())
                    .readerWithView(CustomJson.Shallow.class);

            jsonGenerator.setCodec(mapper);
            jsonGenerator.writeObject(object);
        }

    }
}
