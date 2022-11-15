package ir.jashakouri.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.jashakouri.data.enums.Deleted;
import ir.jashakouri.data.utils.CalenderUtils;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jashakouri on 24.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonIgnore
    private Timestamp updatedAt;

    @Transient
    private String jalaliCreatedAt;

    @JsonIgnore
    public String getJalaliCreatedAt() {
        return CalenderUtils.getJalaliTime(createdAt);
    }

    @Transient
    private String jalaliUpdatedAt;

    @JsonIgnore
    public String getJalaliUpdatedAt() {
        return CalenderUtils.getJalaliTime(updatedAt);
    }

    @Column(name = "deleted", length = 1, nullable = false)
    @JsonIgnore
    private Deleted deleted = Deleted.InActive;

}
