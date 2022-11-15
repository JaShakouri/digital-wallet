package ir.jashakouri.data.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * @author jashakouri on 24.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
public class Paging {
    private int totalElements;
    private int totalPages;
    private int pageSize;
    private int pageNumber;
    private boolean first;
    private boolean last;

    public Paging build(Page<?> data) {
        this.first = data.isFirst();
        this.last = data.isLast();
        this.pageSize = data.getSize();
        this.pageNumber = data.getNumber();
        this.totalPages = data.getTotalPages();
        this.totalElements = data.getNumberOfElements();

        if (first && last)
            return null;

        return this;
    }
}
