package com.vasilyev.kuickhackjvm.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import androidx.core.net.toFile
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.model.Document
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun uriToBase64(uri: Uri, contentResolver: ContentResolver): String {
    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

    return bitmapToBase64(bitmap)
}

fun bitmapToBase64(bitmap: Bitmap): String{
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun base64ToBitmap(string: String): Bitmap{
    val bytes = Base64.decode(string, Base64.DEFAULT)

    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun uriToFile(context: Context, uri: Uri, documentName: String): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, documentName)

    inputStream?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }

    return file
}

