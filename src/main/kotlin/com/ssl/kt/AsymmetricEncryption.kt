package com.ssl.kt

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit


class AsymmetricEncryption(keySize: Int, algorithm: CipherHelper.Algorithm) {

    private var certificate: X509CertificateHolder
    private val encoder = Base64.getEncoder()

    private var keyPair: KeyPair

    init {
        val keyPairGenerator = KeyPairGenerator.getInstance(algorithm.name)
        keyPairGenerator.initialize(keySize)
        keyPair = keyPairGenerator.generateKeyPair()

        // from yesterday to 5 years
        val validityBeginDate = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
        val validityEndDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5 * 365))

        // GENERATE THE X509 CERTIFICATE
        val certBuilder = X509v3CertificateBuilder(
            X500Name(Config.issuer),
            BigInteger.valueOf(System.currentTimeMillis()),
            validityBeginDate,
            validityEndDate,
            X500Name(Config.subject),
            SubjectPublicKeyInfo.getInstance(keyPair.public.encoded)
        )
        val builder = JcaContentSignerBuilder("SHA256withRSA")
        val signer: ContentSigner = builder.build(keyPair.private)
        certificate = certBuilder.build(signer)
    }

    private fun public(): PublicKey {
        return keyPair.public
    }

    private fun private(): PrivateKey {
        return keyPair.private
    }

    fun encodedPublicKey(): String {
        return encoder.encodeToString(public().encoded)
    }

    fun encodedPrivateKey(): String {
        return encoder.encodeToString(private().encoded)
    }

    fun encodedCertificate(): String {
        return encoder.encodeToString(certificate.encoded)
    }

    companion object{

        fun retrievePublicKey(key: ByteArray, algorithm: CipherHelper.Algorithm): PublicKey {
            val ks = X509EncodedKeySpec(key)
            val kf = KeyFactory.getInstance(algorithm.name)
            return kf.generatePublic(ks)
        }

        fun retrievePrivateKey(key: ByteArray, algorithm: CipherHelper.Algorithm): PrivateKey {
            val ks = PKCS8EncodedKeySpec(key)
            val kf = KeyFactory.getInstance(algorithm.name)
            return kf.generatePrivate(ks)
        }
    }

}