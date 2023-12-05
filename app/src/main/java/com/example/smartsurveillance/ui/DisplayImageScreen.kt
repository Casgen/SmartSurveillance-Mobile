package com.example.smartsurveillance.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun DisplayImageScreen(
    fileName: String
) {

    val imgFile = File("${LocalContext.current.filesDir.absolutePath}/${fileName}");

    Image(
        painter = rememberAsyncImagePainter(model = imgFile),
        contentDescription = "Javascript",
        modifier = Modifier.fillMaxSize()
    )
}