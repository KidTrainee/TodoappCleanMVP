package vn.bfc.todoappcleanmvp.tasks;

import android.support.annotation.NonNull;

import java.util.List;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.UseCaseHandler;
import vn.bfc.todoappcleanmvp.domain.model.Task;
import vn.bfc.todoappcleanmvp.domain.usecase.UCActivateTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCClearCompleteTasks;
import vn.bfc.todoappcleanmvp.domain.usecase.UCCompleteTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCGetTasks;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksPresenter implements TasksContract.Presenter {

//    private final TasksContract.View mTasksView;
    private final TasksFragment mTasksView;
    private final UCGetTasks mUcGetTasks;
    private final UCCompleteTask mUcCompleteTask;
    private final UCActivateTask mUcActivateTask;
    private final UCClearCompleteTasks mUcClearCompleteTasks;
    private final UseCaseHandler mUseCaseHandler;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public TasksPresenter(@NonNull UseCaseHandler useCaseHandler,
                          @NonNull TasksContract.View tasksView,
                          @NonNull UCGetTasks ucGetTasks,
                          @NonNull UCCompleteTask ucCompleteTask,
                          @NonNull UCActivateTask ucActivateTask,
                          @NonNull UCClearCompleteTasks ucClearCompleteTasks) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandler cannot be null");
        mTasksView = (TasksFragment) checkNotNull(tasksView, "tasksView cannot be null!");
        mUcGetTasks = checkNotNull(ucGetTasks, "getTask cannot be null!");
        mUcCompleteTask = checkNotNull(ucCompleteTask, "completeTask cannot be null!");
        mUcActivateTask = checkNotNull(ucActivateTask, "activateTask cannot be null!");
        mUcClearCompleteTasks = checkNotNull(ucClearCompleteTasks, "clearCompleteTasks cannot be null!");

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

    @Override
    public void setFiltering(TasksFilterType filterType) {
        mCurrentFiltering = filterType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void openTaskDetails(Task task) {
        checkNotNull(task, "task cannot be null!");
        mTasksView.showTaskDetailsUI(task.getId());
    }

    @Override
    public void completeTask(Task task) {

    }

    @Override
    public void activateTask(Task task) {

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

    private void showFilterLabel() {

    }

    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTasksView.showNoCompletedTasks();
                break;
            default:
                mTasksView.showNoTasks();
                break;
        }
    }

}
