package main.java.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.java.manager.TaskManager;
import main.java.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        switch (method) {
            case "GET": {
                handleGet(exchange, path);
                break;
            }
            case "POST": {
                handlePost(exchange);
                break;
            }
            case "DELETE": {
                handleDelete(exchange, path);
                break;
            }
            default:
                sendIncorrectMethod(exchange, method);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String response;
        if (Pattern.matches("^/tasks/\\d+$", path)) {
            try {
                int id = getIdFromQuery(exchange);
                getTaskManager().removeTask(id);
                response = "Задача удалена!";
                sendText(exchange, response, 200);
            } catch (RuntimeException e) {
                sendNotFound(exchange);
            }
        } else {
            sendInternalServerError(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String response;
        response = readBody(exchange);
        Task task = getGson().fromJson(response, Task.class);
        try {
            if (task.getId() == null) {
                task = getTaskManager().addTask(task);
                response = getGson().toJson(task);
                sendText(exchange, response, 200);
            } else {
                if (getTaskManager().updateTask(task) == null) {
                    sendNotFound(exchange);
                } else {
                    sendText(exchange, response, 201);
                }
            }
        } catch (RuntimeException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String response;
        if (Pattern.matches("^/tasks$", path)) {
            List<Task> tasks = getTaskManager().getAllTasks();
            response = getGson().toJson(tasks);
            sendText(exchange, response, 200);
        } else if (Pattern.matches("^/tasks/\\d+$", path)) {
            int id = getIdFromQuery(exchange);
            try {
                Task task = getTaskManager().getTaskById(id).orElseThrow();
                response = getGson().toJson(task);
                sendText(exchange, response, 200);
            } catch (RuntimeException e) {
                sendNotFound(exchange);
            }
        } else {
            sendInternalServerError(exchange);
        }
    }
}