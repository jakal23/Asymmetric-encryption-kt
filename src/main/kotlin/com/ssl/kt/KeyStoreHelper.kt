package com.ssl.kt

import com.ssl.kt.util.EndLineString
import com.ssl.kt.util.KeyStore

object KeyStoreHelper {

    fun retrieveEncodedKeys(): Pair<String, String> {
        if (!isKeysExist())
            generateAndStoreKeys()
        return readFromKeyStore()
    }

    private fun isKeysExist(): Boolean {
        return KeyStore.isFileExist(Config.publicKeyName)
                && KeyStore.isFileExist(Config.privateKeyName)
    }

    private fun readFromKeyStore(): Pair<String, String> {
        val encodedPublicKey = KeyStore.read(Config.publicKeyName)
        val encodedPrivateKey = KeyStore.read(Config.privateKeyName)
        return Pair(encodedPublicKey, encodedPrivateKey)
    }

    private fun generateAndStoreKeys(){
        val asymmetricEncryption = AsymmetricEncryption(Config.size, Config.algorithm)

        val encodedPublicKey = asymmetricEncryption.encodedPublicKey()
        val encodedPrivateKey = asymmetricEncryption.encodedPrivateKey()
        val certificate = asymmetricEncryption.encodedCertificate()

        KeyStore.write(encodedPublicKey, Config.publicKeyName)
        KeyStore.write(encodedPrivateKey, Config.privateKeyName)
        KeyStore.write(certificate, Config.certificateName)

        KeyStore.writeWithNote(encodedPublicKey, "_" + Config.publicKeyName, EndLineString.publicKey)
        KeyStore.writeWithNote(encodedPrivateKey, "_" + Config.privateKeyName, EndLineString.privateKey)
        KeyStore.writeWithNote(certificate, "_" + Config.certificateName, EndLineString.certificate)
    }
}