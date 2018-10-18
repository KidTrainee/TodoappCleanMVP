package vn.bfc.todoappcleanmvp.tasks;

import vn.bfc.todoappcleanmvp.BasePresenter;
import vn.bfc.todoappcleanmvp.BaseView;

public interface TasksContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        boolean isActive();
    }
    interface Presenter extends BasePresenter {

        void loadTasks(boolean forceUpdate);
    }
}
