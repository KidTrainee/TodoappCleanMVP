package vn.bfc.todoappcleanmvp.domain.usecase;

import android.support.annotation.NonNull;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UCDeleteTask extends UseCase<UCDeleteTask.RequestValues, UCDeleteTask.ResponseValue> {

    private final TasksRepository mTasksRepository;

    public UCDeleteTask(@NonNull TasksRepository tasksRepository) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mTasksRepository.deleteTask(values.getTaskId());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final String mTaskId;

        public RequestValues(@NonNull String taskId) {
            mTaskId = checkNotNull(taskId, "taskId cannot be null!");
        }

        public String getTaskId() {
            return mTaskId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue { }
}
