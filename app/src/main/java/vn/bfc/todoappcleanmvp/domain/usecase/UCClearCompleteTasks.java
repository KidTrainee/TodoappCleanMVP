package vn.bfc.todoappcleanmvp.domain.usecase;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UCClearCompleteTasks extends UseCase<UCClearCompleteTasks.RequestValues, UCClearCompleteTasks.ResponseValue> {
    private final TasksRepository mTasksRepository;

    public UCClearCompleteTasks(TasksRepository tasksRepository) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");    }


    @Override
    protected void executeUseCase(RequestValues values) {
        mTasksRepository.clearCompletedTasks();
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues { }

    public static class ResponseValue implements UseCase.ResponseValue { }
}
