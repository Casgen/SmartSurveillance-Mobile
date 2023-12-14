package com.example.smartsurveillance.ui

import android.util.Log
import com.example.smartsurveillance.services.SSMessagingService
import com.google.firebase.messaging.FirebaseMessaging
import com.smartsurveillance_mobile.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SettingsViewModel() : BaseViewModel() {

    private val _obtainedToken = MutableStateFlow<String?>(null)
    val obtainedToken = _obtainedToken.asStateFlow()

    init {
        loadToken()
    }

    suspend fun retrieveToken(): String? = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    SSMessagingService::class.toString(),
                    "Fetching FCM registration token failed!",
                    task.exception
                )
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            Log.d(SSMessagingService::class.toString(), token)

            continuation.resume(token)
        }
    }

    private fun loadToken() {
        launch {
            val token = retrieveToken()
            _obtainedToken.emit(token)
        }
    }
}