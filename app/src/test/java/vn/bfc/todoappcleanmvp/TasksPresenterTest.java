package vn.bfc.todoappcleanmvp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import vn.bfc.todoappcleanmvp.data.source.TasksDataSource;
import vn.bfc.todoappcleanmvp.data.source.TasksRepository;
import vn.bfc.todoappcleanmvp.domain.filter.FilterFactory;
import vn.bfc.todoappcleanmvp.domain.model.Task;
import vn.bfc.todoappcleanmvp.domain.usecase.UCActivateTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCClearCompleteTasks;
import vn.bfc.todoappcleanmvp.domain.usecase.UCCompleteTask;
import vn.bfc.todoappcleanmvp.domain.usecase.UCGetTasks;
import vn.bfc.todoappcleanmvp.tasks.TasksContract;
import vn.bfc.todoappcleanmvp.tasks.TasksFilterType;
import vn.bfc.todoappcleanmvp.tasks.TasksPresenter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

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

    private TasksPresenter mTasksPresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation.
        // To inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mTasksPresenter = givenTasksPresenter();

    }

    private TasksPresenter givenTasksPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        UCGetTasks getTasks = new UCGetTasks(mTasksRepository, new FilterFactory());
        UCCompleteTask completeTask = new UCCompleteTask(mTasksRepository);
        UCActivateTask activateTask = new UCActivateTask(mTasksRepository);
        UCClearCompleteTasks clearCompleteTasks = new UCClearCompleteTasks(mTasksRepository);

        return new TasksPresenter(useCaseHandler, mTasksView, getTasks, completeTask, activateTask,
                clearCompleteTasks);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        mTasksPresenter = givenTasksPresenter();

        // Then the presenter is set to the view
        verify(mTasksView).setPresenter(mTasksPresenter);
    }
    @Test
    public void loadAllTasksFromRepositoryAndLoadIntoView() {
        // Given an initialized TasksPresenter with initialized tasks
        // When loading of Tasks is requested
        mTasksPresenter.setFiltering(TasksFilterType.ALL_TASKS);
        mTasksPresenter.loadTasks(true);

        // Callback is captured and invoked with stubbed tasks
        verify(mTasksRepository).getTasks(mLoadTasksCallbackCaptor.capture());
        mLoadTasksCallbackCaptor.getValue().onTasksLoaded(TASKS);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(mTasksView);
        inOrder.verify(mTasksView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all tasks are shown in UI
        inOrder.verify(mTasksView).setLoadingIndicator(false);
        ArgumentCaptor<List> showTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mTasksView).showTasks(showTasksArgumentCaptor.capture());
        assertTrue(showTasksArgumentCaptor.getValue().size() == 3);
    }



}
