package vn.bfc.todoappcleanmvp.tasks.domain.filter;

import java.util.ArrayList;
import java.util.List;

import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;

/**
 * Returns all the tasks from a list of {@link Task}s.
 */
public class FilterAllTaskFilter implements TaskFilter {
    @Override
    public List<Task> filter(List<Task> tasks) {
        return new ArrayList<>(tasks);
    }

}
