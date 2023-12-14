package com.example.smartsurveillance.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smartsurveillance_mobile.ui.base.State
import org.koin.androidx.compose.getViewModel


@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = getViewModel(),
    context: Context
) {
    var isTokenShown = remember {
        mutableStateOf(false)
    }

    val state = settingsViewModel.state.collectAsState()
    val token = settingsViewModel.obtainedToken.collectAsState()

    Column {
        TextButton(
            modifier = Modifier
                .fillMaxWidth().background(MaterialTheme.colors.background),
            onClick = {
                isTokenShown.value = !isTokenShown.value
            },
            contentPadding = PaddingValues(20.dp),
        ) {
            Text(
                text = "Show FCM Token", color = Color.Black
            )
        }
    }

    if (isTokenShown.value) {
        AlertDialog(
            onDismissRequest = { isTokenShown.value = !isTokenShown.value },
            buttons = {
                Row {
                    TextButton(onClick = { isTokenShown.value = !isTokenShown.value }) {
                        Text("Ok")
                    }

                    TextButton(onClick = {
                        val clipboardManager =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("", token.value)
                        clipboardManager.setPrimaryClip(clip)
                    }) {
                        Text("Copy")
                    }
                }
            },
            text = {
                when (val result = state.value) {
                    State.None, State.Loading -> {
                        CircularProgressIndicator()
                    }

                    is State.Failure -> {
                        Button(onClick = { result.repeat() }) {
                            Text("ViewModel Failure!")
                        }
                    }

                    is State.Success -> {
                        token.value?.let { token ->
                            Text(token)
                        } ?: run {
                            Text(text = "Failed to obtain the token")
                        }
                    }
                }

            },
            title = { Text("FCM Token") }
        )
    }
}