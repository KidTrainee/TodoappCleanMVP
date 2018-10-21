package vn.bfc.todoappcleanmvp;

public class TestUseCaseScheduler implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <R extends UseCase.ResponseValue> void notifyResponse(R response,
                                                                 UseCase.UseCaseCallback<R> useCaseCallback) {
        useCaseCallback.onSuccess(response);
    }

    @Override
    public <R extends UseCase.ResponseValue> void onError(
            UseCase.UseCaseCallback<R> useCaseCallback) {
        useCaseCallback.onError();
    }
}
