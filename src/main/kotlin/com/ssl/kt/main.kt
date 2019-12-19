package com.ssl.kt

import java.util.*


fun main(){
    val plainText = "Hello".toByteArray()
    println("Text for encrypting: ${String(plainText)}")

    val helper = CipherHelper(Config.algorithm, Config.cipherMode, Config.padding)

    val keys = KeyStoreHelper.retrieveEncodedKeys()
    val publicKey = helper.retrievePublicKey(keys.first)
    val privateKey = helper.retrievePrivateKey(keys.second)
    println("Public key: ${keys.first}")
    println("Private key: ${keys.second}")

    val encryptedPlainText = helper.encrypt(publicKey, plainText)
    val decryptedPlainText = helper.decrypt(privateKey, Base64.getDecoder().decode(encryptedPlainText.toByteArray()))
    println("Encrypted text: $encryptedPlainText")
    println("Decrypted text: $decryptedPlainText")

    val signature = helper.sign(privateKey,plainText)
    val isVerified = helper.verify(publicKey, signature, plainText)
    println("Signed text: $signature")
    println("Verified: $isVerified")
}



