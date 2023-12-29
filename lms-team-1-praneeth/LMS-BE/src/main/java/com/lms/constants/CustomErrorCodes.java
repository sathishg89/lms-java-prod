package com.lms.constants;

public enum CustomErrorCodes {

	// DB Error Codes
	USER_NOT_FOUND("10001", "Given user email Id not found in the database."),
	COURSE_NOT_FOUND("10002", "Course not found in the database with the courseId."),
	EMAIL_ALREADY_EXIST("10003", "Given Email Id already exists in the database."),
	USER_ALREADY_ENROLLED("10004", "User already enrolled with the same course."),
	USER_ALREADY_EXIST("10005", "User already Exist."), COURSE_ALREADY_EXIST("10006", "Course already Exist."),

	// Missing value in Request pay load Codes
	MISSING_IMAGE("10010", "Missing image."), MISSING_EMAIL_ID("10011", "Missing emai id."),
	MISSING_MODULE("10012", "Missing module. "), MISSING_USER_NAME("10013", "Missing user name."),
	INVALID_DETAILS("10014", "InValid Details ."), INVALID_EMAIL("10015", "Email Is Not Valid ."),
	INVALID_PASSWORD("10016", "InValid Password  .");

	private String errorCode;
	private String errorMsg;

	private CustomErrorCodes(String errorCode, String errorMsg) {
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public String getErrorCode() {
		return this.errorCode;
	}
}