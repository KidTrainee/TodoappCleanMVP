package vn.bfc.todoappcleanmvp.tasks.domain.filter;

import java.util.List;

import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;

public interface TaskFilter {

    List<Task> filter(List<Task> tasks);
}
