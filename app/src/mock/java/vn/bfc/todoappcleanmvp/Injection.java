package vn.bfc.todoappcleanmvp;

import android.content.Context;
import android.support.annotation.NonNull;

import vn.bfc.todoappcleanmvp.data.FakeTasksRemoteDataSource;
import vn.bfc.todoappcleanmvp.data.source.local.TasksLocalDataSource;
import vn.bfc.todoappcleanmvp.data.source.local.TodoDatabase;
import vn.bfc.todoappcleanmvp.data.source.remote.TasksDataSource;
import vn.bfc.todoappcleanmvp.data.source.remote.TasksRepository;
import vn.bfc.todoappcleanmvp.tasks.domain.usecase.UCActivateTask;
import vn.bfc.todoappcleanmvp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link TasksDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(
                FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(
                        new AppExecutors(),
                        TodoDatabase.getInstance(context).taskDao()));
    }

    public static UCActivateTask provideActivateTask(@NonNull Context context) {
        return new UCActivateTask(Injection.provideTasksRepository(context));
    }

}
