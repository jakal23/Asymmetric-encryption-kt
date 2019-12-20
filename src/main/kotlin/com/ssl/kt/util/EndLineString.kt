package com.ssl.kt.util

class EndLineString(text: String) {

   val begin = "-----BEGIN $text-----\n"

   val end = "-----END $text-----\n"

    companion object{
        val certificate = EndLineString("CERTIFICATE")
    }
}