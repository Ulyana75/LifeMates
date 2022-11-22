package ru.ulyanaab.lifemates.domain.common.interactor

import android.content.Context
import android.net.Uri
import com.uploadcare.android.library.api.UploadcareClient
import com.uploadcare.android.library.callbacks.UploadFileCallback
import com.uploadcare.android.library.upload.FileUploader
import javax.inject.Inject

class UploadPhotoInteractor @Inject constructor(
    private val context: Context
) {

    fun uploadPhoto(
        imageUri: Uri,
        uploadFileCallback: UploadFileCallback
    ) {
        val client = UploadcareClient("4f043604928cd86cd1ab")
        val uploader = FileUploader(client, imageUri, context).store(true)
        uploader.uploadAsync(uploadFileCallback)
    }
}
