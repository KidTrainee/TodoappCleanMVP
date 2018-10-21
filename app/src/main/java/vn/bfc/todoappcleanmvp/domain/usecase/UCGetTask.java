package vn.bfc.todoappcleanmvp.domain.usecase;

import android.support.annotation.NonNull;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.data.source.TasksDataSource;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;
import vn.bfc.todoappcleanmvp.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class UCGetTask extends UseCase<UCGetTask.RequestValues, UCGetTask.ResponseValue> {
    private final TasksRepository mTasksRepository;

    public UCGetTask(@NonNull TasksRepository tasksRepository) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mTasksRepository.getTask(values.getTaskId(), new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (task != null) {
                    ResponseValue responseValue = new ResponseValue(task);
                    getUseCaseCallback().onSuccess(responseValue);
                } else {
                    getUseCaseCallback().onError();
                }
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
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

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Task mTask;

        public ResponseValue(@NonNull Task task) {
            mTask = checkNotNull(task, "task cannot be null!");
        }

        public Task getTask() {
            return mTask;
        }
    }

}
