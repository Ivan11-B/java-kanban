package main.java.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.exception.ErrorResponse;
import main.java.manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public Gson getGson() {
        return gson;
    }

    protected void sendText(HttpExchange exchange, String text, Integer code) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("Некорректное значение ID", 500, exchange.getRequestURI().getPath());
        String text = getGson().toJson(errorResponse);
        sendText(exchange, text, errorResponse.getErrorCode());
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("Не найден ID", 404, exchange.getRequestURI().getPath());
        String text = getGson().toJson(errorResponse);
        sendText(exchange, text, errorResponse.getErrorCode());
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse("Время уже занято", 406, exchange.getRequestURI().getPath());
        String text = getGson().toJson(errorResponse);
        sendText(exchange, text, errorResponse.getErrorCode());
        exchange.close();
    }

    protected void sendIncorrectMethod(HttpExchange exchange, String method) throws IOException {
        String text = getGson().toJson(String.format("Обработка метода %s не предусмотрена", method));
        sendText(exchange, text, 405);
        exchange.close();
    }

    protected String readBody(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected int getIdFromQuery(HttpExchange httpExchange) {
        try {
            String[] id = httpExchange.getRequestURI().getPath().split("/");
            return Integer.parseInt(id[2]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}