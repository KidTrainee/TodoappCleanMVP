package vn.bfc.todoappcleanmvp;

import android.content.Context;
import android.support.annotation.NonNull;

import vn.bfc.todoappcleanmvp.data.source.local.TasksLocalDataSource;
import vn.bfc.todoappcleanmvp.data.source.local.TodoDatabase;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;
import vn.bfc.todoappcleanmvp.domain.filter.FilterFactory;
import vn.bfc.todoappcleanmvp.domain.usecase.UCActivateTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCClearCompleteTasks;
import vn.bfc.todoappcleanmvp.domain.usecase.UCCompleteTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCDeleteTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCGetTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCGetTasks;
import vn.bfc.todoappcleanmvp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        TodoDatabase database = TodoDatabase.getInstance(context);
        return TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(),
                        database.taskDao()));
    }

    public static UCGetTasks provideGetTasks(@NonNull Context context) {
        return new UCGetTasks(provideTasksRepository(context), new FilterFactory());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static UCGetTask provideGetTask(Context context) {
        return new UCGetTask(Injection.provideTasksRepository(context));
    }

    public static UCCompleteTask provideCompleteTasks(Context context) {
        return new UCCompleteTask(Injection.provideTasksRepository(context));
    }

    public static UCActivateTask provideActivateTask(Context context) {
        return new UCActivateTask(Injection.provideTasksRepository(context));
    }

    public static UCClearCompleteTasks provideClearCompleteTasks(Context context) {
        return new UCClearCompleteTasks(Injection.provideTasksRepository(context));
    }

    public static UCDeleteTask provideDeleteTask(Context context) {
        return new UCDeleteTask(Injection.provideTasksRepository(context));
    }=
}
