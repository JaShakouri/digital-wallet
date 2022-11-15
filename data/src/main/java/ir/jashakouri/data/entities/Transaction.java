package ir.jashakouri.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import ir.jashakouri.data.encrypt.AES.AesEncryptor;
import ir.jashakouri.data.enums.TransactionMethod;
import ir.jashakouri.data.enums.TransactionStatus;
import ir.jashakouri.data.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private Wallet wallet;

    @Column(name = "description")
    @Convert(converter = AesEncryptor.class)
    private String description;

    @Column(name = "amount", nullable = false)
    @Convert(converter = AesEncryptor.class)
    private Long amount;

    @Column(name = "percent")
    @Convert(converter = AesEncryptor.class)
    private Long percent = 0L;

    @Column(name = "token", unique = true)
    @Convert(converter = AesEncryptor.class)
    private String token;

    @Column(name = "factor_id")
    @Convert(converter = AesEncryptor.class)
    private String factorId;

    @Column(name = "invoiceURL")
    @Convert(converter = AesEncryptor.class)
    private String invoiceURL;

    @Column(name = "expire_date", updatable = false)
    private Timestamp expireDate = new Timestamp(System.currentTimeMillis() + (10 * 60 * 1000));

    @Column(name = "type", nullable = false)
    @Convert(converter = AesEncryptor.class)
    private TransactionType type;

    @Column(name = "method", nullable = false)
    @Convert(converter = AesEncryptor.class)
    private TransactionMethod method;

    @Column(name = "status", length = 1, nullable = false)
    private TransactionStatus status;

    public String getDescription() {
        return isExpireTransaction() ? "your transaction canceled by system cause your time for payment has expired" : description;
    }

    public String getToken() {
        return isExpireTransaction() ? null : token;
    }

    public String getInvoiceURL() {
        return isExpireTransaction() ? null : invoiceURL;
    }

    public TransactionStatus getStatus() {
        return isExpireTransaction() ? TransactionStatus.Expired : status;
    }

    private boolean isExpireTransaction() {
        if (status == TransactionStatus.Success) return false;
        return System.currentTimeMillis() >= expireDate.getTime();
    }
}
