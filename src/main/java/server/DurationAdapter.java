package main.java.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.value("");
            return;
        }
        jsonWriter.value(Long.toString(duration.toMinutes()));
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String json = jsonReader.nextString();
        if (json == null || json.isEmpty()) {
            return null;
        }
        return Duration.ofMinutes(Integer.parseInt(json));
    }
}
