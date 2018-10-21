package vn.bfc.todoappcleanmvp.taskdetail;

import android.support.annotation.NonNull;

import vn.bfc.todoappcleanmvp.BasePresenter;
import vn.bfc.todoappcleanmvp.BaseView;

public interface TaskDetailContract {
    interface View extends BaseView<Presenter> {

        void showMissingTask();

        void setLoadingIndicator(boolean isActive);

        boolean isActive();

        void hideTitle();

        void showTitle(@NonNull String title);

        void hideDescription();

        void showDescription(@NonNull String description);

        void showCompletionStatus(boolean complete);

        void showEditTask(String taskId);

        void showTaskDeleted();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();
    }

    interface Presenter extends BasePresenter {

        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();
    }
}
