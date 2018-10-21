package vn.bfc.todoappcleanmvp.domain.filter;

import java.util.List;

import vn.bfc.todoappcleanmvp.domain.model.Task;

public interface TaskFilter {

    List<Task> filter(List<Task> tasks);
}
