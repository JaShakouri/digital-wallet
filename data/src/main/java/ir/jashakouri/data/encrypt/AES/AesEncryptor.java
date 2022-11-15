package ir.jashakouri.data.encrypt.AES;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
@Converter
@Slf4j(topic = "LOG_AesEncryptor")
public class AesEncryptor implements AttributeConverter<Object, String> {

    private final byte[] KEY = "**********".getBytes();

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) return null;
        try {
            byte[] bytes = SerializationUtils.serialize(attribute);
            if (bytes != null)
                return Base64.getEncoder().encodeToString(
                        getCipher(Cipher.ENCRYPT_MODE).doFinal(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @NotNull
    private Cipher getCipher(int encryptMode)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Key key = new SecretKeySpec(KEY, "AES");
        String ALGORITHM = "**********";
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(encryptMode, key);
        return c;
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            byte[] bytes = getCipher(Cipher.DECRYPT_MODE)
                    .doFinal(Base64.getDecoder().decode(dbData));
            return SerializationUtils.deserialize(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
