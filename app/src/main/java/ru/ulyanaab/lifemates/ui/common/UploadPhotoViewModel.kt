package ru.ulyanaab.lifemates.ui.common

import android.net.Uri
import com.uploadcare.android.library.api.UploadcareFile
import com.uploadcare.android.library.callbacks.UploadFileCallback
import com.uploadcare.android.library.exceptions.UploadcareApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ulyanaab.lifemates.di.AppScope
import ru.ulyanaab.lifemates.domain.common.interactor.UploadPhotoInteractor
import javax.inject.Inject

open class UploadPhotoViewModel @Inject constructor(
    private val uploadPhotoInteractor: UploadPhotoInteractor,
) {

    private val _progressValue = MutableStateFlow(0f)
    val progressValue: StateFlow<Float> = _progressValue.asStateFlow()

    private val _isProgressVisible = MutableStateFlow(false)
    val isProgressVisible: StateFlow<Boolean> = _isProgressVisible.asStateFlow()

    private val _isFailure = MutableStateFlow(false)
    val isFailure: StateFlow<Boolean> = _isFailure.asStateFlow()

    private val _linkStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    val linkStateFlow: StateFlow<String?> = _linkStateFlow.asStateFlow()

    fun onImagePickedFromGallery(imageUri: Uri) {
        _isProgressVisible.value = true
        _isFailure.value = false

        uploadPhotoInteractor.uploadPhoto(
            imageUri = imageUri,
            uploadFileCallback = object : UploadFileCallback {
                override fun onFailure(e: UploadcareApiException) {
                    _isProgressVisible.value = false
                    _isFailure.value = true
                }

                override fun onProgressUpdate(
                    bytesWritten: Long,
                    contentLength: Long,
                    progress: Double
                ) {
                    _progressValue.value = progress.toFloat()
                }

                override fun onSuccess(result: UploadcareFile) {
                    _isFailure.value = false
                    _isProgressVisible.value = false
                    _linkStateFlow.value = buildLink(result)
                }

            }
        )
    }

    private fun buildLink(file: UploadcareFile): String? {
        return file.originalFilename?.let {
            val filename = it.replace(" ", "_")
            "https://ucarecdn.com/${file.uuid}/$filename"
        }
    }
}
