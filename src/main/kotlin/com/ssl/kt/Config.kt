package com.ssl.kt

object Config {

    const val dir = "./keys"
    const val publicKeyName = "public_key.pb"
    const val privateKeyName = "private_key.pr"
    const val certificateName = "certificate.cer"

    const val size = 2048
    const val padding = "PKCS1Padding"
    val algorithm = CipherHelper.Algorithm.RSA
    val cipherMode = CipherHelper.CipherMode.ECB

}