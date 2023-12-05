package com.example.smartsurveillance.ui.async.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.smartsurveillance_mobile.ui.base.State
import org.koin.androidx.compose.getViewModel

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = getViewModel(),
    parentController: NavHostController,
    onNavigateDisplay: (String) -> Unit,
) {

    val images = viewModel.capturedImages.collectAsState()
    val state = viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {

        when (val result = state.value) {
            State.None, State.Loading -> {
                CircularProgressIndicator()
            }

            is State.Failure -> {
                Text("Failed to download the files!")
            }

            is State.Success -> {
                // List
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(images.value) { item ->
                        println("Created an Image")
                        Card(
                            modifier = Modifier
                                .size(120.dp)
                                .clickable {
                                    onNavigateDisplay(item.name)
                                },
                            backgroundColor = MaterialTheme.colors.background
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = item),
                                contentDescription = "Javascript",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }

    }


}