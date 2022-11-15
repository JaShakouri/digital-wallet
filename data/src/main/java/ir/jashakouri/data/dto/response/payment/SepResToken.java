package ir.jashakouri.data.dto.response.payment;

public record SepResToken(String errorDesc, String errorCode, int status, String token) {
}