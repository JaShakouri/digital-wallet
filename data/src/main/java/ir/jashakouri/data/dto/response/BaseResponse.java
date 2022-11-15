package ir.jashakouri.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author jashakouri on 23.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private String status = HttpStatus.OK.getReasonPhrase();

    private int statusCode = HttpStatus.OK.value();

    private Object body;

    private Paging page;

    private String error;

    public BaseResponse setContent(Page<?> data) {
        this.body = data.getContent();
        this.page = new Paging().build(data);
        return this;
    }
}
