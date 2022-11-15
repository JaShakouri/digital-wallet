package ir.jashakouri.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * @author jashakouri on 23.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseMock<T> {
    private String status = HttpStatus.OK.getReasonPhrase();

    private int statusCode = HttpStatus.OK.value();

    private T body;

    private Paging page;

    private String error;

}
