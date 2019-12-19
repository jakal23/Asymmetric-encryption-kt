package com.ssl.kt

import java.security.*
import java.util.*
import javax.crypto.Cipher


class CipherHelper(private val algorithm: Algorithm, mode: CipherMode, padding: String) {

    private val cipher = Cipher.getInstance(
        String.format("%s/%s/%s", algorithm.name, mode.name, padding
        )
    )
    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()


    fun retrievePublicKey(publicKeyString: String): PublicKey {
        return AsymmetricEncryption.retrievePublicKey(decoder.decode(publicKeyString.toByteArray()), algorithm)
    }

    fun retrievePrivateKey(privateKeyString: String): PrivateKey {
        return AsymmetricEncryption.retrievePrivateKey(decoder.decode(privateKeyString.toByteArray()), algorithm)
    }

    fun encrypt(publicKey: PublicKey, plaintext: ByteArray): String {
        return try {
            val encrypted = doFinal(Cipher.ENCRYPT_MODE, publicKey, plaintext)
            String(encoder.encode(encrypted))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun decrypt(privateKey: PrivateKey, plaintext: ByteArray): String {
        return try {
            val decrypted = doFinal(Cipher.DECRYPT_MODE, privateKey, plaintext)
            String(decrypted)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun sign(privateKey: PrivateKey, plaintext: ByteArray): String {
        val privateSignature = Signature.getInstance("SHA256withRSA")
        privateSignature.initSign(privateKey)
        privateSignature.update(plaintext)
        val signature = privateSignature.sign()
        return encoder.encodeToString(signature)
    }

    fun verify(publicKey: PublicKey, signature: String, plaintext: ByteArray): Boolean {
        val publicSignature = Signature.getInstance("SHA256withRSA")
        publicSignature.initVerify(publicKey)
        publicSignature.update(plaintext)
        val signatureBytes = decoder.decode(signature)
        return publicSignature.verify(signatureBytes)
    }

    private fun doFinal(opmode: Int, key: Key, plaintext: ByteArray): ByteArray {
        cipher.init(opmode, key)
        return cipher.doFinal(plaintext)
    }

    enum class CipherMode{
        ECB, /* - Electronic Code book*/
        CBC, /* - Cipher Block Chaining*/
        CFB, /* - Cipher Feedback*/
        OFB, /* - Output Feedback*/
        CTR; /* - Counter*/
    }

    enum class Algorithm{
        DSA, DH, RSA
    }
}