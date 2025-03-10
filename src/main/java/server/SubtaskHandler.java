package main.java.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.java.manager.TaskManager;
import main.java.tasks.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
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
        if (Pattern.matches("^/subtasks/\\d+$", path)) {
            try {
                int id = getIdFromQuery(exchange);
                getTaskManager().removeSubtask(id);
                response = "Подзадача удалена!";
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
        Subtask subtask = getGson().fromJson(response, Subtask.class);
        try {
            if (subtask.getId() == null) {
                subtask = getTaskManager().addSubtask(subtask);
                if (subtask == null) {
                    sendNotFound(exchange);
                } else {
                    response = getGson().toJson(subtask);
                    sendText(exchange, response, 200);
                }
            } else {
                if (getTaskManager().updateSubTask(subtask) == null) {
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
        if (Pattern.matches("^/subtasks$", path)) {
            List<Subtask> subtasks = getTaskManager().getAllSubTasks();
            response = getGson().toJson(subtasks);
            sendText(exchange, response, 200);
        } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
            int id = getIdFromQuery(exchange);
            try {
                Subtask subtask = getTaskManager().getSubTaskById(id).orElseThrow();
                response = getGson().toJson(subtask);
                sendText(exchange, response, 200);
            } catch (RuntimeException e) {
                sendNotFound(exchange);
            }
        } else {
            sendInternalServerError(exchange);
        }
    }
}