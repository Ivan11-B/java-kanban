package main.java.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.manager.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import main.java.tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedHandlerTest {

    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager taskManager = taskServer.getTaskManager();
    Gson gson = taskServer.getGson();

    PrioritizedHandlerTest() throws IOException {
    }

    @BeforeEach
    public  void setUp() {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskServer.startServer();
    }

    @AfterEach
    public  void shutDown() {
        taskServer.stopServer();
    }

    @Test
    public void testGetPrioritized() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW,
                LocalDateTime.now().plusMinutes(100), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        taskJson = gson.toJson(task2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        url = URI.create("http://localhost:8080/epics");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 3,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/prioritized");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(responseBody, taskListType);

        assertNotNull(history, "Лист пустой");
        assertEquals(3, history.size(), "Некорректное количество задач");
    }
}