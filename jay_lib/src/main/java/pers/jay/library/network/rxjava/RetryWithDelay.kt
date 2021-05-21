package pers.jay.library.network.rxjava

import io.reactivex.Observable
import io.reactivex.functions.Function
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * @Author RookieJay
 * @Time 2021/5/21 14:33
 * @description rxJava异常重试处理：根据传入参数确定重试次数（默认3次）和间隔时间（默认3s）
 */
class RetryWithDelay(
    private val maxRetryCount: Int = 3,
    private val retryDelayMillis: Long = 3000L
) : Function<Observable<out Throwable?>, Observable<Any>> {

    @Throws(Exception::class)
    override fun apply(observable: Observable<out Throwable?>): Observable<Any> {
        return observable
            .zipWith(Observable.range(1, maxRetryCount + 1), { throwable, index ->
                Wrapper(index, throwable)
            })
            .flatMap { wrapper ->
                val t = wrapper.throwable
                if ((t is ConnectException
                            || t is SocketTimeoutException
                            || t is TimeoutException
                            || t is HttpException)
                    && wrapper.retryCount < maxRetryCount + 1
                ) {
                    Observable.timer(retryDelayMillis * wrapper.retryCount, TimeUnit.MILLISECONDS)
                } else {
                    Observable.error(wrapper.throwable)
                }
            }
    }

    internal class Wrapper(var retryCount: Int, var throwable: Throwable?)
}