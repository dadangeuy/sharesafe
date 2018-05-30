package com.sharesafe.shared;

import com.google.common.primitives.Bytes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class RsaUtil {
    private static final int key_size = 4096;
    private static final int encrypt_block = 501;
    private static final int decrypt_block = 512;
    private static final KeyPairGenerator generator = initGenerator();
    private static final KeyFactory factory = initFactory();
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    private static KeyPairGenerator initGenerator() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(key_size);
            return generator;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyFactory initFactory() {
        try {
            return KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generatePair() {
        return generator.generateKeyPair();
    }

    public static String encode(PrivateKey key) {
        return encoder.encodeToString(new PKCS8EncodedKeySpec(key.getEncoded()).getEncoded());
    }

    public static PrivateKey decodePrivate(String encoded) throws InvalidKeySpecException {
        return factory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(encoded)));
    }

    public static String encode(PublicKey key) {
        return encoder.encodeToString(new X509EncodedKeySpec(key.getEncoded()).getEncoded());
    }

    public static PublicKey decodePublic(String encoded) throws InvalidKeySpecException {
        return factory.generatePublic(new X509EncodedKeySpec(decoder.decode(encoded)));
    }

    public static String encodeEncrypt(byte[] data, PublicKey key) {
        return encoder.encodeToString(encrypt(data, key));
    }

    private static byte[] encrypt(byte[] data, PublicKey key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return execute(cipher, data, encrypt_block);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decodeDecrypt(String data, PrivateKey key) {
        return decrypt(decoder.decode(data), key);
    }

    private static byte[] decrypt(byte[] data, PrivateKey key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return execute(cipher, data, decrypt_block);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] execute(Cipher cipher, byte[] original, int blockSize) throws BadPaddingException, IllegalBlockSizeException {
        List<Byte> result = new LinkedList<>();
        for (int i = 0; i < original.length; i += blockSize) {
            int size = Math.min(blockSize, original.length - i);
            byte[] block = splitBlock(original, i, size);
            byte[] finalBlock = cipher.doFinal(block);
            result.addAll(Bytes.asList(finalBlock));
        }
        return Bytes.toArray(result);
    }

    private static byte[] splitBlock(byte[] original, int startIdx, int blockSize) {
        return Arrays.copyOfRange(original, startIdx, startIdx + blockSize);
    }
}
