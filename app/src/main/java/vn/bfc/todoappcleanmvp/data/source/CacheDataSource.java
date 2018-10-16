package vn.bfc.todoappcleanmvp.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vn.bfc.todoappcleanmvp.tasks.domain.model.Task;

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
}
