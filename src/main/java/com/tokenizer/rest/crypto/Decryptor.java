package com.tokenizer.rest.crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokenizer.rest.request.AuthRequest;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class Decryptor {

    private Decryptor() {

    }

    public static AuthRequest decrypt(String encrypted, String key) throws Exception {
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(Base64.decodeBase64(encrypted)));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(decrypted, AuthRequest.class);
    }

}
