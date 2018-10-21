package vn.bfc.todoappcleanmvp.taskdetail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import vn.bfc.todoappcleanmvp.Injection;
import vn.bfc.todoappcleanmvp.R;
import vn.bfc.todoappcleanmvp.util.ActivityUtils;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested task id
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new TaskDetailPresenter(
                Injection.provideUseCaseHandler(),
                taskId,
                taskDetailFragment,
                Injection.provideUCGetTask(getApplicationContext()),
                Injection.provideUCCompleteTask(getApplicationContext()),
                Injection.provideUCActivateTask(getApplicationContext()),
                Injection.provideUCDeleteTask(getApplicationContext()));

    }
}
