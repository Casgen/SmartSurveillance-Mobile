package com.example.smartsurveillance.ui.async.gallery

import com.smartsurveillance_mobile.data.FirebaseRepository
import com.smartsurveillance_mobile.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class GalleryViewModel(
    private val repo: FirebaseRepository,
    private val filesDir: File
) : BaseViewModel() {

    private val _capturedImages = MutableStateFlow<List<File>>(emptyList())
    val capturedImages = _capturedImages.asStateFlow()

    init {
        loadImages()
    }

    private fun loadImages() {
        launch {
            val images: List<File> = if (repo.downloadedImages.isEmpty()) {
                repo.downloadImages(filesDir)
            } else {
                repo.downloadedImages
            }
            _capturedImages.emit(images)
        }
    }
}