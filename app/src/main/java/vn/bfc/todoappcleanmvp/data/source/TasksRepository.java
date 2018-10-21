package vn.bfc.todoappcleanmvp.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import vn.bfc.todoappcleanmvp.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between
 * locally persisted data and data obtained from the server,
 * by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTasksLocalDataSource;

    private final CacheDataSource mCachedDataSource;

    // Prevent direct instantiation.
    private TasksRepository(@NonNull TasksDataSource tasksRemoteDataSource,
                            @NonNull TasksDataSource tasksLocalDataSource,
                            @NonNull CacheDataSource cacheDataSource) {
        mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
        mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
        mCachedDataSource = cacheDataSource;
    }


    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksRemoteDataSource the backend data source
     * @param tasksLocalDataSource  the device storage data source
     * @return the {@link TasksRepository} instance
     */
    public static TasksRepository getInstance(TasksDataSource tasksRemoteDataSource,
                                              TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksRemoteDataSource,
                    tasksLocalDataSource,
                    new CacheDataSource());
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(TasksDataSource, TasksDataSource)}
     * to create a new instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source,
     * whichever is available first.
     * <p>
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if
     * all data sources fail to get the data.
     */
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedDataSource.isDataAvailable()) {
            callback.onTasksLoaded(mCachedDataSource.getTasks());
            return;
        }

        if (mCachedDataSource.isDirty()) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    mCachedDataSource.refreshCache(tasks);
                    callback.onTasksLoaded(mCachedDataSource.getTasks());
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveTask(Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.saveTask(task);
        mTasksLocalDataSource.saveTask(task);
        mCachedDataSource.saveTask(task);
    }

    @Override
    public void deleteAllTasks() {
        mTasksRemoteDataSource.deleteAllTasks();
        mTasksLocalDataSource.deleteAllTasks();
        mCachedDataSource.deleteAllTasks();
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTasksWithId(taskId));
    }

    @Override
    public void activateTask(Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.activateTask(task);
        mTasksLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());

        // Do in memory cache update to keep the app UI up-to-date
        mCachedDataSource.saveTask(activeTask);
    }


    private Task getTasksWithId(String id) {
        checkNotNull(id);
        return mCachedDataSource.getTask(id);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                mCachedDataSource.refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(mCachedDataSource.getTasks());
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        mTasksLocalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTasksLocalDataSource.saveTask(task);
        }
    }

    public void refreshTasks() {
        mCachedDataSource.setDirty(true);
    }

    @Override
    public void completeTask(Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.completeTask(task);
        mTasksLocalDataSource.completeTask(task);
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        mCachedDataSource.saveTask(completedTask);
    }

    @Override
    public void completeTask(String taskId) {
        checkNotNull(taskId);
        completeTask(getTasksWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks();
        mTasksLocalDataSource.clearCompletedTasks();

        mCachedDataSource.clearCompleteTasks();
    }

    @Override
    public void deleteTask(String taskId) {

    }

    @Override
    public void getTask(String taskId, GetTaskCallback callback) {

    }

}
