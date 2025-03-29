package com.marcello0140.ciatriadsecure.helper

import android.annotation.SuppressLint
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object SecurityHelper {
    private const val SECRET_KEY = "mySecretKey"
    private const val SALT = "mySaltValue"

    @SuppressLint("GetInstance")
    fun encryptData(data: String): String {
        val secretKey = generateKey()
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun hashTransaction(transaction: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(transaction.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    private fun generateKey(): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(SECRET_KEY.toCharArray(), SALT.toByteArray(), 65536, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    @SuppressLint("GetInstance")
    fun decryptData(encryptedData: String): String {
        val secretKey = generateKey()
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }
}