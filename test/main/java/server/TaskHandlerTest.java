package main.java.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.manager.TaskManager;
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

class TaskHandlerTest {

    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager taskManager = taskServer.getTaskManager();
    Gson gson = taskServer.getGson();

    TaskHandlerTest() throws IOException {
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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Task task2 = new Task(1,"Задача обновленная", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        taskJson = gson.toJson(task2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task taskById = gson.fromJson(response.body(), Task.class);

        assertEquals("Задача 1", taskById.getName(), "Некорректное имя");
        assertEquals("Описание 1", taskById.getDescription(), "Некорректное описание");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
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
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(responseBody, taskListType);

        assertNotNull(tasks, "Лист пустой");
        assertEquals(2, tasks.size(), "Некорректное количество задач");
    }

    @Test
    public void testRemoveTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testIntersectionTimeTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Task task2 = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        taskJson = gson.toJson(task2);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    public void testNotFoundTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        url = URI.create("http://localhost:8080/tasks/11");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}