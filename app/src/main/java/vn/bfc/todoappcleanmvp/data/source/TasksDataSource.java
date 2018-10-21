package vn.bfc.todoappcleanmvp.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import vn.bfc.todoappcleanmvp.domain.model.Task;

public interface TasksDataSource {

    void getTasks(@NonNull LoadTasksCallback callback);

    void saveTask(Task task);

    void deleteAllTasks();

    void activateTask(@NonNull String taskId);

    void activateTask(Task task);

    void completeTask(String completedTask);

    void completeTask(Task task);

    void clearCompletedTasks();

    void deleteTask(String taskId);

    void getTask(String taskId, GetTaskCallback callback);

    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

}
