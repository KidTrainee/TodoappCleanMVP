package vn.bfc.todoappcleanmvp;

import android.content.Context;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link TasksDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        TodoDatabase database = TodoDatabase.getInstance(context);
        return TasksRepository.getInstance(
                FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(),
                        database.taskDao()));
    }
}
