package vn.bfc.todoappcleanmvp.taskdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import vn.bfc.todoappcleanmvp.UseCase;
import vn.bfc.todoappcleanmvp.UseCaseHandler;
import vn.bfc.todoappcleanmvp.domain.model.Task;
import vn.bfc.todoappcleanmvp.domain.usecase.UCActivateTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCCompleteTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCDeleteTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCGetTask;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TaskDetailFragment mTaskDetailView;
    private final UseCaseHandler mUseCaseHandler;
    private final UCGetTask mGetTask;
    private final UCCompleteTask mCompleteTask;
    private final UCActivateTask mActivateTask;
    private final UCDeleteTask mDeleteTask;

    @Nullable
    private String mTaskId;

    public TaskDetailPresenter(@NonNull UseCaseHandler useCaseHandler,
                               @Nullable String taskId,
                               @NonNull TaskDetailContract.View taskDetailView,
                               @NonNull UCGetTask getTask,
                               @NonNull UCCompleteTask completeTask,
                               @NonNull UCActivateTask activateTask,
                               @NonNull UCDeleteTask deleteTask) {
        mTaskId = taskId;
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null!");
        mTaskDetailView = (TaskDetailFragment) checkNotNull(taskDetailView, "taskDetailView cannot be null!");
        mGetTask = checkNotNull(getTask, "getTask cannot be null!");
        mCompleteTask = checkNotNull(completeTask, "completeTask cannot be null!");
        mActivateTask = checkNotNull(activateTask, "activateTask cannot be null!");
        mDeleteTask = checkNotNull(deleteTask, "deleteTask cannot be null!");
        mTaskDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openTask();
    }

    private void openTask() {
       if (checkTaskId()) {

           mTaskDetailView.setLoadingIndicator(true);

           mUseCaseHandler.execute(mGetTask, new UCGetTask.RequestValues(mTaskId),
                   new UseCase.UseCaseCallback<UCGetTask.ResponseValue>() {
                       @Override
                       public void onSuccess(UCGetTask.ResponseValue response) {
                           Task task = response.getTask();

                           // the view may not be able to handle UI update anymore
                           if (!mTaskDetailView.isActive()) {
                               return;
                           }

                           mTaskDetailView.setLoadingIndicator(false);
                           showTask(task);
                       }

                       @Override
                       public void onError() {

                       }
                   });
       }
    }

    private void showTask(@NonNull Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            mTaskDetailView.hideTitle();
        } else {
            mTaskDetailView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            mTaskDetailView.hideDescription();
        } else {
            mTaskDetailView.showDescription(description);
        }

        mTaskDetailView.showCompletionStatus(task.isCompleted());
    }

    @Override
    public void editTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTaskDetailView.showEditTask(mTaskId);
    }

    @Override
    public void deleteTask() {
        mUseCaseHandler.execute(mDeleteTask, new UCDeleteTask.RequestValues(mTaskId),
                new UseCase.UseCaseCallback<UCDeleteTask.ResponseValue>() {
                    @Override
                    public void onSuccess(UCDeleteTask.ResponseValue response) {
                        mTaskDetailView.showTaskDeleted();
                    }

                    @Override
                    public void onError() {
                        // show error, log, etc.
                    }
                });
    }

    @Override
    public void completeTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
            mUseCaseHandler.execute(mCompleteTask, new UCCompleteTask.RequestValues(mTaskId),
                    new UseCase.UseCaseCallback<UCCompleteTask.ResponseValue>() {
                        @Override
                        public void onSuccess(UCCompleteTask.ResponseValue response) {
                            mTaskDetailView.showTaskMarkedComplete();
                        }

                        @Override
                        public void onError() {
                            // show error, log, etc.
                        }
                    });
    }

    @Override
    public void activateTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }

        if (checkTaskId()) {
            mUseCaseHandler.execute(mActivateTask, new UCActivateTask.RequestValues(mTaskId),
                    new UseCase.UseCaseCallback<UCActivateTask.ResponseValue>() {
                        @Override
                        public void onSuccess(UCActivateTask.ResponseValue response) {
                            mTaskDetailView.showTaskMarkedActive();
                        }

                        @Override
                        public void onError() {
                            // show error, log, etc.
                        }
                    });
        }
    }

    /**
     * check taskId empty. If emptyOrNull then show message to users.
     * @return true if not empty, false otherwise
     */
    private boolean checkTaskId() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return false;
        }
        return true;
    }
}
