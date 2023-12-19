package com.lms.constants;

public enum CustomErrorCodes {

	// DB Error Codes
	USER_NOT_FOUND("10001", "Given user email Id not found in the database."),
	COURSE_NOT_FOUND("10002", "Course not found in the database with the courseId."),
	EMAIL_ALREADY_EXIST("10003", "Given Email Id already exists in the database."),
	TOPIC_NOT_FOUND("10004", "Given Topic not found in the database with the given topicId."),
	MODULE_ALREADY_EXIST("10005", "Module name already exist in the database with the same course."),
	USER_ALREADY_ENROLLED("10006", "User already enrolled with the same course."),
	USER_ALREADY_EXIST("10007", "User already Exist."),COURSE_ALREADY_EXIST("10008", "Course already Exist."),

	// Missing value in Request payload Codes
	MISSING_IMAGE("10021", "Missing image."), MISSING_TOPIC("10022", "Missing topic name."),
	MISSING_TITLE("10023", "Missing title name."), MISSING_EMAIL_ID("10024", "Missing emai id."),
	MISSING_PASSWORD("10025", "Missing password."), MISSING_MODULE("10026", "Missing module name."),
	MISSING_USER_NAME("10027", "Missing user name."), MISSING_DESCRIPTION("10028", "Missing description."),
	INVALID_DETAILS("10029", "InValid Details ."), INVALID_EMAIL("10030", "Email Is Not Valid ."),
	INVALID_PASSWORD("10030", "InValid Password  .");

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