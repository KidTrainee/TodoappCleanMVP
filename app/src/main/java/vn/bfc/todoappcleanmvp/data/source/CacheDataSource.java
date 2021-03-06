package vn.bfc.todoappcleanmvp.data.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vn.bfc.todoappcleanmvp.domain.model.Task;

public class CacheDataSource {

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Task> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data
     * is requested. This variable has package local visibility so it
     * can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    public boolean isDirty() {
        return mCacheIsDirty;
    }

    public boolean isDataAvailable() {
        return mCachedTasks != null && !isDirty();
    }

    public List<Task> getTasks() {
        return new ArrayList<>(mCachedTasks.values());
    }

    public void saveTask(Task task) {
        // Do in memory cache update to keep the app UI up to dat
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    public void deleteAllTasks() {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    public void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    public boolean isEmpty() {
        return mCachedTasks.isEmpty();
    }

    public Task getTask(String id) {
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }

    public void setDirty(boolean isDirty) {
        mCacheIsDirty = isDirty;
    }


    public void clearCompleteTasks() {
        getCacheTasks();
        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    private void getCacheTasks() {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
    }
}
