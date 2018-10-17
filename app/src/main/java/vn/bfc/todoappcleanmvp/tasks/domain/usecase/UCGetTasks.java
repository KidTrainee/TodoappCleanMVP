package vn.bfc.todoappcleanmvp.tasks.domain.usecase;

import android.support.annotation.NonNull;

import java.util.List;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.data.source.remote.TasksDataSource;
import vn.bfc.todoappcleanmvp.data.source.remote.TasksRepository;
import vn.bfc.todoappcleanmvp.tasks.TasksFilterType;
import vn.bfc.todoappcleanmvp.tasks.domain.filter.FilterFactory;
import vn.bfc.todoappcleanmvp.tasks.domain.filter.TaskFilter;
import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class UCGetTasks extends UseCase<UCGetTasks.RequestValues, UCGetTasks.ResponseValue> {

    private final TasksRepository mTasksRepository;

    private final FilterFactory mFilterFactory;

    public UCGetTasks(TasksRepository tasksRepository, FilterFactory filterFactory) {
        this.mTasksRepository = tasksRepository;
        this.mFilterFactory = filterFactory;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            mTasksRepository.refreshTasks();
        }

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                TasksFilterType currentFiltering = values.getCurrentFiltering();
                TaskFilter taskFilter = mFilterFactory.create(currentFiltering);

                List<Task> tasksFiltered = taskFilter.filter(tasks);
                ResponseValue responseValue = new ResponseValue(tasksFiltered);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final TasksFilterType mCurrentFiltering;
        private final boolean mForceUpdate;

        public RequestValues(TasksFilterType currentFiltering, boolean forceUpdate) {
            this.mCurrentFiltering = currentFiltering;
            this.mForceUpdate = forceUpdate;
        }

        public TasksFilterType getCurrentFiltering() {
            return mCurrentFiltering;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Task> mTasks;

        public ResponseValue(@NonNull List<Task> tasks) {
            mTasks = checkNotNull(tasks, "tasks cannot be null!");
        }

        public List<Task> getTasks() {
            return mTasks;
        }

    }
}
