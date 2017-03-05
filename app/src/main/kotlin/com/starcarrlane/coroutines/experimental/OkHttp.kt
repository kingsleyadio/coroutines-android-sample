package com.starcarrlane.coroutines.experimental

import kotlinx.coroutines.experimental.CancellableContinuation
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * @author ADIO Kingsley O.
 * @since 03 Mar, 2017
 */

suspend fun Call.await(): Response {
    return suspendCancellableCoroutine { cont: CancellableContinuation<Response> ->
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!cont.isCancelled) cont.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) cont.resume(response)
                else cont.resumeWithException(HttpException(response))
            }

        })
        registerOnCompletion(cont)
    }

}

private fun Call.registerOnCompletion(continuation: CancellableContinuation<*>) {
    continuation.invokeOnCompletion {
        if (continuation.isCancelled) // only cancel call if coroutine was canceled
            try { cancel() }
            catch (ignore: Throwable) { }
    }
}


class HttpException(@Transient val response: Response) : Exception("HTTP ${response.code()} ${response.message()}") {

    val code: Int = response.code()
    override val message: String = response.message()
}
