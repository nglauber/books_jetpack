package dominando.android.domain

import dominando.android.domain.executor.PostExecutionThread
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class FlowableUseCase<T, in Params> constructor(
        private val postExecutionThread: PostExecutionThread
) {
    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseFlowable(params: Params? = null): Flowable<T>

    open fun execute(
            params: Params? = null,
            onNext: (T) -> Unit,
            onError: (e: Throwable) -> Unit,
            onComplete: (() -> Unit)? = null) {
        val flowable = this.buildUseCaseFlowable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.scheduler)
        addDisposable(flowable.subscribe(
                { t: T ->
                    onNext.invoke(t)
                },
                { error ->
                    onError.invoke(error)
                },
                {
                    onComplete?.invoke()
                }
        ))
    }

    fun dispose() {
        disposables.clear()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}