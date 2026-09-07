package com.vn.bomnuocv1.core.network

import android.util.Base64
import org.json.JSONObject

object JwtHelper {

    /**
     * Checks if the given JWT token is expired.
     * Returns true if token is null, empty, invalid, or expired.
     */
    fun isExpired(token: String?): Boolean {
        if (token.isNullOrBlank()) return true
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return true

            val payloadBytes = Base64.decode(
                parts[1],
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )
            val payloadJson = String(payloadBytes, Charsets.UTF_8)
            val jsonObject = JSONObject(payloadJson)

            val expSeconds = jsonObject.optLong("exp", 0L)
            if (expSeconds == 0L) return false

            val currentEpochSeconds = System.currentTimeMillis() / 1000
            currentEpochSeconds >= expSeconds
        } catch (_: Exception) {
            false
        }
    }
}
