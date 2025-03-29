package com.marcello0140.ciatriadsecure.helper

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object BackupHelper {
    private const val BACKUP_FILE_NAME = "backup.txt"

    fun saveBackup(context: Context, data: String) {
        try {
            val file = File(context.filesDir, BACKUP_FILE_NAME)
            FileOutputStream(file).use {
                it.write(data.toByteArray())
            }
            Log.d("BackupHelper", "Backup berhasil disimpan")
        } catch (e: Exception) {
            Log.e("BackupHelper", "Gagal menyimpan backup", e)
        }
    }
}