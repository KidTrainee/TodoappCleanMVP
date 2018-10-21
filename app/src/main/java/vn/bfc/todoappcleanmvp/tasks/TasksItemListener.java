package vn.bfc.todoappcleanmvp.tasks;

import vn.bfc.todoappcleanmvp.domain.model.Task;

public interface TasksItemListener {

    void onTaskClick(Task clickedTask);

    void onCompleteTaskClick(Task completedTask);

    void onActivateTaskClick(Task activatedTask);
}

