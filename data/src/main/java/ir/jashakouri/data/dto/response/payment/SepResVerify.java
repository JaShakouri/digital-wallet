package ir.jashakouri.data.dto.response.payment;

import lombok.Data;

@Data
public class SepResVerify{
	private String resultDescription;
	private VerifyInfo verifyInfo;
	private int resultCode;
	private boolean success;
}