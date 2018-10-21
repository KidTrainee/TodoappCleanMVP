package vn.bfc.todoappcleanmvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String ARGUMENT_EDIT_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_task_act);
    }
}
