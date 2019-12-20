package com.ssl.kt

object Config {

    const val dir = "./keys"
    const val defLineLength = 64
    const val publicKeyName = "public_key.cer"
    const val privateKeyName = "private_key.cer"
    const val certificateName = "certificate.cer"

    const val issuer = "CN=Vahe Gharibyan"
    const val subject = "CN=IoHouse, OU=Developers, O=ATCC C=Armenia"

    const val size = 2048
    const val padding = "PKCS1Padding"
    val algorithm = CipherHelper.Algorithm.RSA
    val cipherMode = CipherHelper.CipherMode.ECB

}