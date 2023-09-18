package com.example.contactapp

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun loadImageFromUri(contentResolver: ContentResolver, uri: Uri, maxSize: Int = 1024): Bitmap? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)

            // Calculate the inSampleSize value to resize the image
            options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize)

            // Close the input stream and reopen it for decoding
            inputStream.close()
            val newInputStream = contentResolver.openInputStream(uri)

            // Decode the image with the calculated inSampleSize
            options.inJustDecodeBounds = false
            val resizedBitmap = BitmapFactory.decodeStream(newInputStream, null, options)

            // Close the new input stream
            newInputStream?.close()

            resizedBitmap
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
fun encodeBitmapToBase64(bitmap: Bitmap?): String? {
    if (bitmap == null) return null

    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}