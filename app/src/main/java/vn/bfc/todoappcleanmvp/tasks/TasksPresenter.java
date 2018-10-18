package vn.bfc.todoappcleanmvp.tasks;

import android.support.annotation.NonNull;

import java.util.List;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.UseCaseHandler;
import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;
import vn.bfc.todoappcleanmvp.tasks.domain.usecase.UCActivateTask;
import vn.bfc.todoappcleanmvp.tasks.domain.usecase.UCClearCompleteTasks;
import vn.bfc.todoappcleanmvp.tasks.domain.usecase.UCCompleteTasks;
import vn.bfc.todoappcleanmvp.tasks.domain.usecase.UCGetTasks;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksPresenter implements TasksContract.Presenter {

    private final TasksContract.View mTasksView;
    private final UCGetTasks mUcGetTasks;
    private final UCCompleteTasks mUcCompleteTasks;
    private final UCActivateTask mUcActivateTask;
    private final UCClearCompleteTasks mUcClearCompleteTasks;
    private final UseCaseHandler mUseCaseHandler;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public TasksPresenter(@NonNull UseCaseHandler useCaseHandler,
                          @NonNull TasksContract.View tasksView,
                          @NonNull UCGetTasks ucGetTasks,
                          @NonNull UCCompleteTasks ucCompleteTasks,
                          @NonNull UCActivateTask ucActivateTask,
                          @NonNull UCClearCompleteTasks ucClearCompleteTasks) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandler cannot be null");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");
        mUcGetTasks = checkNotNull(ucGetTasks, "getTask cannot be null!");
        mUcCompleteTasks = checkNotNull(ucCompleteTasks, "completeTask cannot be null!");
        mUcActivateTask = checkNotNull(ucActivateTask, "activateTask cannot be null!");
        mUcClearCompleteTasks = checkNotNull(ucClearCompleteTasks,
                "clearCompleteTasks cannot be null!");

        mTasksView.setPresenter(this);

    }


    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTasks(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true);
        }

        UCGetTasks.RequestValues requestValues = new UCGetTasks.RequestValues(mCurrentFiltering, forceUpdate);

        mUseCaseHandler.execute(mUcGetTasks, requestValues,
                new UseCase.UseCaseCallback<UCGetTasks.ResponseValue>() {
                    @Override
                    public void onSuccess(UCGetTasks.ResponseValue response) {
                        List<Task> tasks = response.getTasks();
                        // the view may not be able to handle UI updates anymore
                        if (!mTasksView.isActive()) {
                            return;
                        }

                        if (showLoadingUI) {
                            mTasksView.setLoadingIndicator(false);
                        }

                        processTasks(tasks);
                    }

                    @Override
                    public void onError() {
                        // the view may not be able to handle UI updates anymore
                        if (!mTasksView.isActive()) {
                            return;
                        }
                        mTasksView.showLoadingTasksError();
                    }
                });
    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mTasksView.showTasks(tasks);
            // set the filter label's text.
            showFilterLabel();
        }
    }

}
