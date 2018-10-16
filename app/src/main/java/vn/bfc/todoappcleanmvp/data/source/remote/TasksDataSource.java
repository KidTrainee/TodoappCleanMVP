package vn.bfc.todoappcleanmvp.data.source.remote;

import android.support.annotation.NonNull;

import java.util.List;

import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;

public interface TasksDataSource {

    void getTasks(@NonNull LoadTasksCallback callback);

    void saveTask(Task task);

    void deleteAllTasks();

    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }


    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

}
