package vn.bfc.todoappcleanmvp;

import vn.bfc.todoappcleanmvp.util.EspressoIdlingResource;

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
                mUseCaseScheduler.notifyResponse(response, callback);
            }

            @Override
            public void onError() {
                mUseCaseScheduler.onError(callback);
            }
        });

        // The network request might be handled in a different thread so make sure
        // Espresso knows that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mUseCaseScheduler.execute(() -> {
            useCase.run();
            // This callback may be called twice, once for the cache and once for loading
            // the data from the server API, so we check before decrementing, otherwise
            // it throws "Counter has been corrupted!" exception.
            if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                EspressoIdlingResource.decrement(); // Set app as idle.
            }
        });
    }
}
