package vn.bfc.todoappcleanmvp.taskdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import vn.bfc.todoappcleanmvp.AddEditTaskActivity;
import vn.bfc.todoappcleanmvp.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EXTRA_TASK_ID = "TASK_ID";
    private static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailPresenter mPresenter;

    private TextView mDetailTitle;

    private TextView mDetailDescription;

    private CheckBox mDetailCompleteStatus;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param taskId Parameter 1.
     * @return A new instance of fragment TaskDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskDetailFragment newInstance(String taskId) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_detail, container, false);

        setHasOptionsMenu(true);

        mDetailTitle = root.findViewById(R.id.task_detail_title);
        mDetailDescription = root.findViewById(R.id.task_detail_description);
        mDetailCompleteStatus = root.findViewById(R.id.task_detail_complete);

        // Set up floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task);

        fab.setOnClickListener((v) -> mPresenter.editTask());

        return root;
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        mPresenter = (TaskDetailPresenter) checkNotNull(presenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mPresenter.deleteTask();
                return true;
        }
        return false;
    }

    @Override
    public void showMissingTask() {
        mDetailTitle.setText("");
        mDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void setLoadingIndicator(boolean isActive) {
        if (isActive) {
            mDetailTitle.setText("");
            mDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void hideTitle() {
        mDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(@NonNull String title) {
        mDetailTitle.setVisibility(View.VISIBLE);
        mDetailTitle.setText(title);
    }

    @Override
    public void hideDescription() {
        mDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(@NonNull String description) {
        mDetailDescription.setVisibility(View.VISIBLE);
        mDetailDescription.setText(description);
    }

    @Override
    public void showCompletionStatus(boolean complete) {
        checkNotNull(mDetailCompleteStatus);

        mDetailCompleteStatus.setChecked(complete);
        mDetailCompleteStatus.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                mPresenter.completeTask();
            } else {
                mPresenter.activateTask();
            }
        });
    }

    @Override
    public void showEditTask(String taskId) {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(), getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }
}
