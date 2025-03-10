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

class SubtaskHandlerTest {

    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager taskManager = taskServer.getTaskManager();
    Gson gson = taskServer.getGson();

    SubtaskHandlerTest() throws IOException {
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
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask2 = new Subtask(2,"Подзадача2", "Описание2", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        subtaskJson = gson.toJson(subtask2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/subtasks/2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtaskById = gson.fromJson(response.body(), Subtask.class);

        assertEquals("Подзадача1", subtaskById.getName(), "Некорректное имя");
        assertEquals("Описание1", subtaskById.getDescription(), "Некорректное описание");
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 11, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson2 = gson.toJson(subtask2);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson2))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Subtask> subtasks = gson.fromJson(responseBody, taskListType);

        assertNotNull(subtasks, "Лист пустой");
        assertEquals(2, subtasks.size(), "Некорректное количество задач");
    }

    @Test
    public void testRemoveSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/subtasks/2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testIntersectionTimeSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson2 = gson.toJson(subtask2);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson2))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    public void testNotFoundSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 1,
                LocalDateTime.of(2025, 10, 1, 12, 0), Duration.ofMinutes(55));
        String subtaskJson = gson.toJson(subtask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/subtasks/11");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}