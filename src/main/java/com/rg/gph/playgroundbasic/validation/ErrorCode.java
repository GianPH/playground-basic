package com.rg.gph.playgroundbasic.validation;

public enum ErrorCode {
	ALREADY_EXIST("AlreadyExist");

	private String codeName;

	ErrorCode(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeName() {
		return codeName;
	}
}
