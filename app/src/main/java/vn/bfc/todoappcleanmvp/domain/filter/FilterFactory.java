package vn.bfc.todoappcleanmvp.domain.filter;

import java.util.HashMap;
import java.util.Map;

import vn.bfc.todoappcleanmvp.tasks.TasksFilterType;

/**
 * Factory of {@link TaskFilter}s.
 */
public class FilterFactory {

    private static final Map<TasksFilterType, TaskFilter> mFilters = new HashMap<>();


    public FilterFactory() {
        mFilters.put(TasksFilterType.ALL_TASKS, new AllTaskFilter());
        mFilters.put(TasksFilterType.ACTIVE_TASKS, new ActiveTaskFilter());
        mFilters.put(TasksFilterType.COMPLETED_TASKS, new CompleteTaskFilter());
    }

    public TaskFilter create(TasksFilterType filterType) {
        return mFilters.get(filterType);
    }

}
