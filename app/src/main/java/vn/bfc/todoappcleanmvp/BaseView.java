package vn.bfc.todoappcleanmvp;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);


}
