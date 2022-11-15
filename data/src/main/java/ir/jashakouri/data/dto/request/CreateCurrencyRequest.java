package ir.jashakouri.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jashakouri on 29.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCurrencyRequest {

    @Size(min = 4, max = 32, message = "Name should be between 4 and 16")
    @NotBlank(message = "Name can not be empty")
    @NotNull(message = "Name is requirement")
    private String name;

    private String symbol;
    private String prefix;
    private String postfix;
    private Long usdEquivalent;
}
