package ir.jashakouri.data.utils.encrypt;

import ir.jashakouri.data.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j(topic = "LOG_UtilsEncrypt")
public class EncryptUtils {

    public CompletableFuture<KeyPair> createKeys(User user) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("*****");
        generator.initialize(2048, new SecureRandom(user.getId().toString()
                .getBytes(StandardCharsets.UTF_8)));
        return CompletableFuture.completedFuture(generator.generateKeyPair());
    }

    public static String encrypt(Object object, Key key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("*****");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypt = cipher.doFinal(Objects.requireNonNull(SerializationUtils.serialize(object)));
        return Base64.getEncoder().encodeToString(encrypt);
    }

    public static Object decrypt(String encryptedMessage, Key key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("*****");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return SerializationUtils.deserialize(decrypt);
    }

}
