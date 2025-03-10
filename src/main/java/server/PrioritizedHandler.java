package main.java.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.java.manager.TaskManager;
import main.java.tasks.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            Set<Task> prioritizedTasks = getTaskManager().getPrioritizedTasks();
            String response = getGson().toJson(prioritizedTasks);
            sendText(exchange, response, 200);
        } else {
            sendIncorrectMethod(exchange, method);
        }
    }
}