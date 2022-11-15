package ir.jashakouri.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import ir.jashakouri.data.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Currency extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name", length = 32, unique = true, nullable = false)
    private String name;

    @Column(name = "symbol", length = 12)
    private String symbol;

    @Column(name = "prefix", length = 12)
    private String prefix;

    @Column(name = "postfix", length = 12)
    private String postfix;

    @Column(name = "usd_equivalent", length = 16)
    private Long usdEquivalent;

    @Column(name = "status", length = 1, nullable = false)
    @JsonIgnore
    private Status status = Status.Active;

    public Currency(UUID id) {
        this.id = id;
    }
}
