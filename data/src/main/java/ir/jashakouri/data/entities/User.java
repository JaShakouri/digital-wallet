package ir.jashakouri.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import ir.jashakouri.data.encrypt.AES.AesEncryptor;
import ir.jashakouri.data.enums.Status;
import ir.jashakouri.data.enums.UserType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "full_name", nullable = false)
    @Convert(converter = AesEncryptor.class)
    private String fullName;

    @Column(name = "username", nullable = false, unique = true)
    @Convert(converter = AesEncryptor.class)
    private String username;

    @Column(name = "password", nullable = false)
    @Convert(converter = AesEncryptor.class)
    @JsonIgnore
    private String password;

    @Column(name = "phone_number", nullable = false, unique = true)
    @Convert(converter = AesEncryptor.class)
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    @Convert(converter = AesEncryptor.class)
    private String email;

    @Column(name = "about")
    @Convert(converter = AesEncryptor.class)
    private String about;

    @Column(name = "user_type", length = 16, nullable = false)
    private UserType userType;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Wallet> wallet = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    @ToString.Exclude
    private EncryptKeys encryptKeys;

    @JsonIgnore
    public String getPublicKey() {
        if (encryptKeys == null) return null;
        return Base64.getEncoder().encodeToString(encryptKeys.getPublicKey().getEncoded());
    }

    @JsonIgnore
    public String getPrivateKey() {
        if (encryptKeys == null) return null;
        return Base64.getEncoder().encodeToString(encryptKeys.getPrivateKey().getEncoded());
    }

    @Column(name = "status", length = 1, nullable = false)
    private Status status = Status.Active;

    @Column(name = "encrypt_response", length = 1, nullable = false)
    private Boolean encryptResponse = false;

    public User(UUID id) {
        this.id = id;
    }
}
