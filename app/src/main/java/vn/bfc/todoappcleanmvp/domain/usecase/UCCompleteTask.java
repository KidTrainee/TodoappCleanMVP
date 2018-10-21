package vn.bfc.todoappcleanmvp.domain.usecase;

import android.support.annotation.NonNull;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UCCompleteTask extends UseCase<UCCompleteTask.RequestValues, UCCompleteTask.ResponseValue> {

    private TasksRepository mTasksRepository;

    public UCCompleteTask(TasksRepository tasksRepository) {
        mTasksRepository = tasksRepository;
    }

    @Override
    protected void executeUseCase(RequestValues values) {
        String completedTask = values.getCompletedTask();
        mTasksRepository.completeTask(completedTask);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {
        private final String mCompletedTask;

        public RequestValues(@NonNull String completedTask) {
            mCompletedTask = checkNotNull(completedTask, "completedTask cannot be null");
        }

        public String getCompletedTask() {
            return mCompletedTask;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}
