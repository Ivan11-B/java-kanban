package main.java.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.java.manager.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
        if (Pattern.matches("^/epics/\\d+$", path)) {
            try {
                int id = getIdFromQuery(exchange);
                getTaskManager().removeEpic(id);
                response = "Эпик удален!";
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
        Epic epic = getGson().fromJson(response, Epic.class);
        try {
            if (epic.getId() == null) {
                epic = getTaskManager().addEpic(epic);
                response = getGson().toJson(epic);
                sendText(exchange, response, 200);
            } else {
                if (getTaskManager().updateEpic(epic) == null) {
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
        if (Pattern.matches("^/epics$", path)) {
            List<Epic> epics = getTaskManager().getAllEpics();
            response = getGson().toJson(epics);
            sendText(exchange, response, 200);
        } else if (Pattern.matches("^/epics/\\d+$", path)) {
            int id = getIdFromQuery(exchange);
            try {
                Epic epic = getTaskManager().getEpicById(id).orElseThrow();
                response = getGson().toJson(epic);
                sendText(exchange, response, 200);
            } catch (RuntimeException e) {
                sendNotFound(exchange);
            }
        } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
            int id = getIdFromQuery(exchange);
            if (getTaskManager().getEpicById(id).isPresent()) {
                List<Subtask> subtaskToEpic = getTaskManager().getSubtasksByEpic(id);
                if (subtaskToEpic == null) {
                    response = "Нет подзадач";
                } else {
                    response = getGson().toJson(subtaskToEpic);
                }
                sendText(exchange, response, 200);
            }
        } else {
            sendInternalServerError(exchange);
        }
    }
}
