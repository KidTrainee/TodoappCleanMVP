package vn.bfc.todoappcleanmvp.tasks.domain.filter;

import java.util.ArrayList;
import java.util.List;

import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;

public class CompleteTaskFilter implements TaskFilter {
    @Override
    public List<Task> filter(List<Task> tasks) {
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isCompleted()) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
}
