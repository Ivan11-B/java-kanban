package main.java.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import main.java.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class TimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.value("");
            return;
        }
        jsonWriter.value(localDateTime.format(Task.FORMATTER));
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String json = jsonReader.nextString();
        if (json == null || json.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(json, Task.FORMATTER);
    }
}
