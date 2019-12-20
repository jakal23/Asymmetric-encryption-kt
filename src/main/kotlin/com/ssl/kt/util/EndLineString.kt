package com.ssl.kt.util

class EndLineString(text: String) {

   val begin = "-----BEGIN $text-----\n"

   val end = "-----END $text-----\n"

    companion object{
        val certificate = EndLineString("CERTIFICATE")
        val privateKey = EndLineString("PRIVATE KEY")
        val publicKey = EndLineString("PUBLIC KEY")
    }
}