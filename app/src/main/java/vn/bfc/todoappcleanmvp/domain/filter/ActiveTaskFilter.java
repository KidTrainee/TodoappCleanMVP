package vn.bfc.todoappcleanmvp.domain.filter;

import java.util.ArrayList;
import java.util.List;

import vn.bfc.todoappcleanmvp.domain.model.Task;

public class ActiveTaskFilter implements TaskFilter {
    @Override
    public List<Task> filter(List<Task> tasks) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isActive()) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
}
