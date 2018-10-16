package vn.bfc.todoappcleanmvp.data.source.local;

import android.support.annotation.NonNull;

import java.util.List;

import vn.bfc.todoappcleanmvp.data.source.remote.TasksDataSource;
import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;
import vn.bfc.todoappcleanmvp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksLocalDataSource implements TasksDataSource {

    private static volatile TasksLocalDataSource INSTANCE;

    private TasksDao mTasksDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private TasksLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull TasksDao tasksDao) {
        mAppExecutors = appExecutors;
        mTasksDao = tasksDao;
    }

    public static TasksLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull TasksDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (TasksLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksLocalDataSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Runnable runnable = () -> {
            final List<Task> tasks = mTasksDao.getTasks();
            mAppExecutors.mainThread().execute(() -> {
                if (tasks.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable();
                } else {
                    callback.onTasksLoaded(tasks);
                }
            });
        };
    }

    @Override
    public void saveTask(Task task) {
        checkNotNull(task);
        mAppExecutors.diskIO().execute(() -> mTasksDao.insertTask(task));
    }

    @Override
    public void deleteAllTasks() {
        mAppExecutors.diskIO().execute(() -> mTasksDao.deleteTasks());
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Not required for the local data source because the
        // {@link TasksRepository} handles converting from a {@code taskId}
        // to a {@link task} using its cached data.
    }

    @Override
    public void activateTask(@NonNull Task task) {
        mAppExecutors.diskIO().execute(() -> mTasksDao.updateCompleted(task.getId(), false));
    }
}
