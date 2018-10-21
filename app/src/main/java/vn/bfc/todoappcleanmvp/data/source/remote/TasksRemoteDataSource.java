package vn.bfc.todoappcleanmvp.data.source.remote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import vn.bfc.todoappcleanmvp.data.source.TasksDataSource;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;
import vn.bfc.todoappcleanmvp.data.source.local.TasksLocalDataSource;
import vn.bfc.todoappcleanmvp.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksRemoteDataSource implements TasksDataSource {


    private static TasksRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, Task> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static TasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private TasksRemoteDataSource() {}

    private static void addTask(String title, String description) {
        Task newTask = new Task(title, description);
        TASKS_SERVICE_DATA.put(newTask.getId(), newTask);
    }

    /**
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->
                callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values())),
                SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveTask(Task task) {
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Not required for the remote data source because the {@link TasksRepository}
        // handles converting from a {@code taskId} to a {@link task} using
        // its cached data.
    }

    @Override
    public void activateTask(Task task) {
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        TASKS_SERVICE_DATA.put(task.getId(), activeTask);
    }

    @Override
    public void completeTask(String completedTask) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void completeTask(Task task) {
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASKS_SERVICE_DATA.put(task.getId(), completedTask);
    }

    @Override
    public void clearCompletedTasks() {
        Iterator<Map.Entry<String, Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void deleteTask(String taskId) {
        TASKS_SERVICE_DATA.clear();
    }

    /**
     * Note: {@link GetTaskCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getTask(String taskId, GetTaskCallback callback) {
        final Task task = TASKS_SERVICE_DATA.get(taskId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(() -> callback.onTaskLoaded(task), SERVICE_LATENCY_IN_MILLIS);
    }
}
