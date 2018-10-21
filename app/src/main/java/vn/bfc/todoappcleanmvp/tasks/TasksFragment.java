package vn.bfc.todoappcleanmvp.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import vn.bfc.todoappcleanmvp.taskdetail.TaskDetailActivity;
import vn.bfc.todoappcleanmvp.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksFragment extends Fragment implements TasksContract.View{

    private TasksPresenter mPresenter;

    private TasksAdapter mListAdapter;

    public static TasksFragment newInstance() {

        Bundle args = new Bundle();

        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TasksAdapter(new ArrayList<>(0), mItemListener);
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    TasksItemListener mItemListener = new TasksItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activateTask(activatedTask);
        }
    };


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showTasks(List<Task> tasks) {

    }

    @Override
    public void showNoActiveTasks() {

    }

    @Override
    public void showNoCompletedTasks() {

    }

    @Override
    public void showNoTasks() {

    }

    @Override
    public void showTaskDetailsUI(String taskId) {
        // in it's own Activity, since it makes more sense that way and
        // it gives us the flexibility to show some Intent stubbing.
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = (TasksPresenter) checkNotNull(presenter);
    }
}
