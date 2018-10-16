package vn.bfc.todoappcleanmvp;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {
    private static UseCaseHandler instance;

    private final UseCaseScheduler mUseCaseScheduler;

    public static UseCaseHandler getInstance() {
        if (instance == null) {
            instance = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return instance;
    }

    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        this.mUseCaseScheduler = useCaseScheduler;
    }

    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UseCase.UseCaseCallback<R>() {
            @Override
            public void onSuccess(R response) {
                UseCaseHandler.this.notifyResponse(response, callback);
            }

            @Override
            public void onError() {
                UseCaseHandler.this.notifyError(callback);
            }
        });
    }

    private <R extends UseCase.ResponseValue> void notifyError
            (UseCase.UseCaseCallback<R> callback) {
        mUseCaseScheduler.onError(callback);
    }

    private <R extends UseCase.ResponseValue> void notifyResponse
            (R response, UseCase.UseCaseCallback<R> callback) {
        mUseCaseScheduler.notifyResponse(response, callback);
    }
}
