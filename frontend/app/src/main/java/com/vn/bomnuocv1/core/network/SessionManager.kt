package com.vn.bomnuocv1.core.network

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

    private val isHandlingExpiration = AtomicBoolean(false)

    private val _sessionExpiredEvent = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sessionExpiredEvent: SharedFlow<String> = _sessionExpiredEvent.asSharedFlow()

    /**
     * Triggers a session expiration event if not already handling one.
     * Returns true if this call initiated the event, false if an event is already in progress.
     */
    fun triggerSessionExpired(
        message: String = "Phiên đăng nhập của bạn đã hết hạn. Vui lòng đăng nhập lại."
    ): Boolean {
        return if (isHandlingExpiration.compareAndSet(false, true)) {
            _sessionExpiredEvent.tryEmit(message)
            true
        } else {
            false
        }
    }

    /**
     * Resets the expiration flag once the user has acknowledged or navigated to login.
     */
    fun resetExpirationState() {
        isHandlingExpiration.set(false)
    }
}
