package com.example.ease.model
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudinary.android.BuildConfig
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.example.ease.base.MyApplication
import java.io.File
import java.io.FileOutputStream


class CloudinaryModel {
    companion object {
        private var isInitialized = false
    }

    init{
        val config= mapOf(
            "cloud_name" to com.example.ease.BuildConfig.CLOUD_NAME,
            "api_key" to com.example.ease.BuildConfig.API_KEY,
            "api_secret" to com.example.ease.BuildConfig.API_SECRET
        )
        MyApplication.Globals.context?.let {
            if (!isInitialized) {
                MediaManager.init(it, config)
                MediaManager.get().globalUploadPolicy = GlobalUploadPolicy.defaultPolicy()
                isInitialized = true
            }
        }
    }
    fun uploadImage(bitmap: Bitmap, name : String, onSuccess: (String?) -> Unit, onError: (String?) -> Unit){
        Log.d("Cloudinary", "uploadImage0 ofek ")
        //val context= MyApplication.Globals.context ?:return
        val context = MyApplication.Globals.context
        if (context == null){
            return
        }
        Log.d("Cloudinary", "uploadImage1 ofek ")
        val file :File= bitmap.toFile( context,name )
        Log.d("Cloudinary", "uploadImage2 ofek ")
        MediaManager.get().upload(file.path)
            .option("folder", "images")
            .callback(object: UploadCallback{

                override fun onStart(requestId: String?) {

                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    Log.d("Cloudinary", "uploadImage3 ofek ")
                    val url=resultData["secure_url"] as? String?: ""
                    onSuccess(url)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.d("Cloudinary", "uploadImage4 ofek ")
                    onError(error?.description?: "Unknown error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                }
            })
            .dispatch()
    }
  fun Bitmap.toFile (context: Context, name: String) : File{
        val file= File(context.cacheDir, "image_$name.jpg")
        FileOutputStream(file).use{ stream ->
            Log.d("Cloudinary", "uploadImage5 ofek ")
            this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }
        return file
    }


}