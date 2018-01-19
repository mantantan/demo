package yonyou.esn.openapi.exception;

import yonyou.esn.openapi.common.CodeEnum;

public class BizException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 849781423500989160L;

	private CodeEnum codeEnum;
	
	public BizException(CodeEnum codeEnum){
		super(codeEnum.getContent());
		this.codeEnum = codeEnum;
	}

	public CodeEnum getCodeEnum() {
		return codeEnum;
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(Throwable cause) {
		super(cause);
	}

}
