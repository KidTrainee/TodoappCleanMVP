package vn.bfc.todoappcleanmvp;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import vn.bfc.todoappcleanmvp.data.source.TasksDataSource;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;
import vn.bfc.todoappcleanmvp.domain.model.Task;
import vn.bfc.todoappcleanmvp.tasks.TasksContract;
import vn.bfc.todoappcleanmvp.tasks.TasksPresenter;

/**
 * Unit tests for the implementation of {@link TasksPresenter}
 */
public class TasksPresenterTest {
    private static List<Task> TASKS;

    @Mock
    private TasksRepository mTasksRepository;

    @Mock
    private TasksContract.View mTasksView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values
     * and use them to perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<TasksDataSource.LoadTasksCallback> mLoadTasksCallbackCaptor;

}
