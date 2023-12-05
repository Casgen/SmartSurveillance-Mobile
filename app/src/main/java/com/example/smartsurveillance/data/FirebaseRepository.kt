package com.smartsurveillance_mobile.data

import android.util.Log
import com.google.firebase.storage.component1
import com.google.firebase.storage.component2
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class FirebaseRepository(
    var storage: FirebaseStorage = Firebase.storage,
    var downloadedImages: MutableList<File> = mutableListOf()
) {

    init {
        Log.i("FirebaseRepository", "Repo Initialized")
    }

    suspend fun downloadImages(path: File): List<File> = suspendCoroutine { continuation ->
        downloadedImages.clear()

        storage.reference.listAll().addOnSuccessListener { (items, prefixes) ->
            run {

                for (item in items) {
                    print(item.name)


                    val localFile = File(path, item.name);
                    localFile.createNewFile()

                    item.getFile(localFile).addOnSuccessListener {
                        println("Downloaded file successfully")
                    }.addOnCanceledListener {
                        println("Failed to download the image")
                    }

                    if (!downloadedImages.add(localFile)) {
                        println("Failed to insert a file into the array!")
                    }
                }

                continuation.resume(downloadedImages)
            }
        }.addOnFailureListener { e ->
            println("Failed to download the images! ${e.message}")
        }
    }
}