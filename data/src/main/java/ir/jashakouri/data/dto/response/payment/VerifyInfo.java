package ir.jashakouri.data.dto.response.payment;

import lombok.Data;

@Data
public class VerifyInfo{
	private String straceNo;
	private String hashedPan;
	private int affectiveAmount;
	private String straceDate;
	private String maskedPan;
	private int originalAmount;
	private int terminalNumber;
	private String rRN;
	private String refNum;
}