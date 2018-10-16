package vn.bfc.todoappcleanmvp.tasks.domain.usecase;

import android.support.annotation.NonNull;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.data.source.remote.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Marks a task as active (not completed yet).
 */
public class UCActivateTask extends UseCase<UCActivateTask.RequestValues, UCActivateTask.ResponseValue> {

    private final TasksRepository mTaskRepository;

    public UCActivateTask(@NonNull TasksRepository tasksRepository) {
        mTaskRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        String activeTask = values.getActivateTask();
        mTaskRepository.activateTask(activeTask);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final String mActivateTask;

        public RequestValues(@NonNull String activateTask) {
            mActivateTask = checkNotNull(activateTask, "activateTask cannot be null!");
        }

        public String getActivateTask() { return mActivateTask; }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
