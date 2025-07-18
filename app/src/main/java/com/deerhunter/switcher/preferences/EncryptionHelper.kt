package com.deerhunter.switcher.preferences

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.BLOCK_MODE_GCM
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Singleton
class EncryptionHelper @Inject constructor() {
    private val cipher by lazy {
        Cipher.getInstance(TRANSFORMATION)
    }
    private val keyStore by lazy {
        KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    }
    private val keyGenerator by lazy {
        KeyGenerator.getInstance(KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
    }

    private val mutex = Mutex()

    suspend fun encryptData(text: String): String = withContext(Dispatchers.IO) {
        mutex.withLock {
            val secretKey = generateSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedData = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            val iv = cipher.iv
            val secureString =
                iv.joinToString(BYTES_TO_STRING_SEPARATOR) + IV_TO_STRING_SEPARATOR + encryptedData.joinToString(
                    BYTES_TO_STRING_SEPARATOR
                )
            secureString
        }
    }

    suspend fun decryptData(secureString: String) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val (ivString, encryptedString) = secureString.split(IV_TO_STRING_SEPARATOR, limit = 2)
            val iv = ivString.split(BYTES_TO_STRING_SEPARATOR).map { it.toByte() }.toByteArray()
            val encryptedData = encryptedString.split(BYTES_TO_STRING_SEPARATOR).map { it.toByte() }.toByteArray()
            val secretKey = getSecretKey()
            val gcmParameterSpec = GCMParameterSpec(TAG_SIZE, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
            cipher.doFinal(encryptedData).toString(Charsets.UTF_8)
        }
    }

    private fun generateSecretKey(): SecretKey {
        val keyEntry = keyStore.getEntry(KEY_ALIAS, null)
        return if (keyEntry == null) {
            keyGenerator.apply {
                init(
                    KeyGenParameterSpec
                        .Builder(KEY_ALIAS, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                        .setBlockModes(BLOCK_MODE_GCM)
                        .setEncryptionPaddings(ENCRYPTION_PADDING_NONE)
                        .build()
                )
            }.generateKey()
        } else {
            getSecretKey()
        }
    }

    private fun getSecretKey() = (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey

    companion object {
        private const val KEY_ALIAS = "switcher_encryption_key"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val TAG_SIZE = 128 // bits
        private const val BYTES_TO_STRING_SEPARATOR = "|"
        private const val IV_TO_STRING_SEPARATOR= ":iv:"
    }
}
