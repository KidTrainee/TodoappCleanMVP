package vn.bfc.todoappcleanmvp.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

import vn.bfc.todoappcleanmvp.data.source.remote.TasksDataSource;
import vn.bfc.todoappcleanmvp.domain.model.Task;

public class FakeTasksRemoteDataSource implements TasksDataSource {

    private static FakeTasksRemoteDataSource INSTANCE;

    private static final Map<String, Task> TASKS_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeTasksRemoteDataSource() {}

    public static FakeTasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeTasksRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
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
        // not required for the remote data source.
    }

    @Override
    public void activateTask(Task task) {
        Task activeTask = new Task(
                task.getTitle(),
                task.getDescription(),
                task.getId());
        TASKS_SERVICE_DATA.put(task.getId(), activeTask);
    }
}
