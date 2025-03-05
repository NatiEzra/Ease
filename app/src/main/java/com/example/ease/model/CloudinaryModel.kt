package com.example.ease.model
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.cloudinary.android.BuildConfig
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.example.ease.base.MyApplication
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
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
        //val context= MyApplication.Globals.context ?:return
        val context = MyApplication.Globals.context
        if (context == null){
            return
        }
        val file :File= bitmap.toFile( context,name )
        MediaManager.get().upload(file.path)
            .option("folder", "images")
            .callback(object: UploadCallback{

                override fun onStart(requestId: String?) {

                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    val url=resultData["secure_url"] as? String?: ""
                    onSuccess(url)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
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

    fun deleteImage(previousImageUrl: String, onComplete: (Boolean, String?) -> Unit) {
        // שלב 1: הפקת ה־public_id מתוך ה־URL
        val publicId = extractPublicIdFromUrl(previousImageUrl)

        // שלב 2: הרצת הפעולה במחוץ ל־Main Thread (למנוע חסימה של ממשק המשתמש)
        Thread {
            try {
                // קריאה ל־destroy() של Cloudinary
                val result = MediaManager.get()
                    .cloudinary
                    .uploader()
                    .destroy(publicId, emptyMap<String, Any>())

                // אחרי סיום הפעולה - חוזרים ל־Main Thread כדי לדווח ל־onComplete
                Handler(Looper.getMainLooper()).post {
                    // בודקים מה החזירה Cloudinary
                    if (result["result"] == "ok") {
                        // אם קיבלנו "ok", סימן שהמחיקה הצליחה
                        onComplete(true, null)
                    } else {
                        // אם לא, מחזירים false ואת ההודעה (לרוב "not found" או שגיאה אחרת)
                        onComplete(false, result["result"] as? String)
                    }
                }
            } catch (e: Exception) {
                // במקרה של חריגה (למשל בעיית רשת) - נדווח על השגיאה ב־onComplete
                Handler(Looper.getMainLooper()).post {
                    onComplete(false, e.message)
                }
            }
        }.start()
    }


    fun extractPublicIdFromUrl(url: String): String {
        // 1) מסירים כל מה שלפני (כולל) המחרוזת "upload/"
        val afterUpload = url.substringAfter("upload/")
        // לדוגמה, אחרי פעולה זו נקבל: "v1738327009/images/x64azxfunlrpskbq1qv0.jpg"

        // 2) מסירים את החלק של הגרסה (vXXXXXX/) על ידי לקיחת המחרוזת שאחרי הסלאש הראשון
        val afterVersion = afterUpload.substringAfter("/")
        // בדוגמה שלנו: "images/x64azxfunlrpskbq1qv0.jpg"

        // 3) מסירים את הסיומת (למשל ".jpg" או ".png")
        return afterVersion.substringBeforeLast(".")
        // נקבל: "images/x64azxfunlrpskbq1qv0"
    }


}