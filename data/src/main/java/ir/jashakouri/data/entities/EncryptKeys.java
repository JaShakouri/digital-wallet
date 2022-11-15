package ir.jashakouri.data.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import ir.jashakouri.data.encrypt.AES.AesEncryptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.security.Key;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EncryptKeys extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID id;

    @OneToOne
    @MapsId
    private User user;

    @Convert(converter = AesEncryptor.class)
    @Column(columnDefinition = "TEXT")
    private Key publicKey;

    @Convert(converter = AesEncryptor.class)
    @Column(columnDefinition = "TEXT")
    private Key privateKey;

}
