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

class EpicHandlerTest {

    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager taskManager = taskServer.getTaskManager();
    Gson gson = taskServer.getGson();

    EpicHandlerTest() throws IOException {
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
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic2 = new Epic(1, "Эпик 2", "Описание 2", TaskStatus.NEW);
        epicJson = gson.toJson(epic2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/epics/1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Epic epicById = gson.fromJson(response.body(), Epic.class);

        assertEquals("Эпик 1", epicById.getName(), "Некорректное имя");
        assertEquals("Описание 1", epicById.getDescription(), "Некорректное описание");
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        epicJson = gson.toJson(epic2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> epics = gson.fromJson(responseBody, taskListType);

        assertNotNull(epics, "Лист пустой");
        assertEquals(2, epics.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetSubtaskToEpic() throws IOException, InterruptedException {
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
        url = URI.create("http://localhost:8080/epics/1/subtasks");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        Type subtaskListType = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> subtasksToEpic = gson.fromJson(responseBody, subtaskListType);

        assertNotNull(subtasksToEpic, "Лист пустой");
        assertEquals(1, subtasksToEpic.size(), "Некорректное количество подзадач");
    }

    @Test
    public void testRemoveEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/epics/1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testNotFoundEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/epics/11");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}