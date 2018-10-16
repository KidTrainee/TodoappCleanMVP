package vn.bfc.todoappcleanmvp.tasks;

import vn.bfc.todoappcleanmvp.BasePresenter;
import vn.bfc.todoappcleanmvp.BaseView;

public interface TasksContract {
    interface View extends BaseView<Presenter> {

    }
    interface Presenter extends BasePresenter {

    }
}
