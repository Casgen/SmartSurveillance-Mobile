package com.example.smartsurveillance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartsurveillance.services.SSMessagingService
import com.example.smartsurveillance.ui.DisplayImageScreen
import com.example.smartsurveillance.ui.SettingsScreen
import com.example.smartsurveillance.ui.SettingsViewModel
import com.example.smartsurveillance.ui.async.gallery.GalleryScreen

@Composable
fun AppContainer(
    controller: NavHostController,
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(20.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Smart Surveillance", color = Color.White)
                Button(
                    modifier = Modifier.size(24.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                    controller.navigateSettings()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = "Settings",
                        tint = Color.White,
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = controller,
            startDestination = DESTINATION_GALLERY,
        ) {
            // Graph navigation

            composable(
                route = DESTINATION_GALLERY,
            ) {
                GalleryScreen(
                    parentController = controller,
                    onNavigateDisplay = { fileName ->
                        controller.navigateDisplayImage(fileName)
                    }
                )
            }

            composable(
                route = DESTINATION_DISPLAY,
                arguments = listOf(navArgument("fileName") { type = NavType.StringType })
            ) { navBackStackEntry ->
                DisplayImageScreen(
                    fileName = navBackStackEntry.arguments?.getString(FILENAME_ARG).orEmpty()
                )
            }

            composable(
                route = DESTINATION_SETTINGS,
            ) {
                SettingsScreen(context = LocalContext.current)
            }

        }

    }

}

private const val FILENAME_ARG = "fileName"

private const val DESTINATION_GALLERY = "gallery"
private const val DESTINATION_DISPLAY = "display/{$FILENAME_ARG}"
private const val DESTINATION_SETTINGS = "settings"


fun NavHostController.navigateGallery() =
    navigate(DESTINATION_GALLERY)

fun NavHostController.navigateSettings() =
    navigate(DESTINATION_SETTINGS)

fun NavHostController.navigateDisplayImage(fileName: String) =
    navigate(DESTINATION_DISPLAY.replaceArg(FILENAME_ARG, fileName))

private fun String.replaceArg(argName: String, value: String) =
    replace("{$argName}", value)

