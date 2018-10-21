package vn.bfc.todoappcleanmvp.tasks;

import java.util.List;

import vn.bfc.todoappcleanmvp.BasePresenter;
import vn.bfc.todoappcleanmvp.BaseView;
import vn.bfc.todoappcleanmvp.domain.model.Task;

public interface TasksContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        boolean isActive();

        void showLoadingTasksError();

        void showTasks(List<Task> tasks);

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showNoTasks();

        void showTaskDetailsUI(String taskId);
    }
    interface Presenter extends BasePresenter {

        void loadTasks(boolean forceUpdate);

        void setFiltering(TasksFilterType filterType);

        TasksFilterType getFiltering();

        void openTaskDetails(Task task);

        void completeTask(Task task);

        void activateTask(Task task);
    }
}
