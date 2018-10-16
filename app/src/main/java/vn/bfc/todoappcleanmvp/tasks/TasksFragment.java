package vn.bfc.todoappcleanmvp.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TasksFragment extends Fragment {
    public static TasksFragment newInstance() {

        Bundle args = new Bundle();

        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
