package vn.bfc.todoappcleanmvp.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Task.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase instance;

    public abstract  TasksDao tasksDao();

    private static final Object sLock = new Object();

    public static TodoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        TodoDatabase.class, "Tasks.db")
                        .build();
            }
            return instance;
        }
    }
}
