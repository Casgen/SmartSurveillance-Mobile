package com.example.smartsurveillance

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartsurveillance.ui.DisplayImageScreen
import com.example.smartsurveillance.ui.async.gallery.GalleryScreen

@Composable
fun AppContainer(
    controller: NavHostController,
) {

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
            arguments = listOf(navArgument("fileName") { type = NavType.StringType})
        ) {navBackStackEntry ->
            DisplayImageScreen(
                fileName = navBackStackEntry.arguments?.getString(FILENAME_ARG).orEmpty()
            )
        }

    }
}

private const val FILENAME_ARG = "fileName"

private const val DESTINATION_GALLERY = "gallery"
private const val DESTINATION_DISPLAY = "display/{$FILENAME_ARG}"


fun NavHostController.navigateGallery() =
    navigate(DESTINATION_GALLERY)

fun NavHostController.navigateDisplayImage(fileName: String) =
    navigate(DESTINATION_DISPLAY.replaceArg(FILENAME_ARG, fileName))

private fun String.replaceArg(argName: String, value: String) =
    replace("{$argName}", value)

