package com.gmail.ezlotnikova.service.constant;

public class ExecutionResult {

    private static int NO_ERROR = 0;

    public static ExecutionResult ok() {
        return new ExecutionResult(ResultTypeEnum.EXECUTED_SUCCESSFULLY, NO_ERROR, "");
    }

    public static ExecutionResult error(int errorCode) {
        return new ExecutionResult(ResultTypeEnum.EXECUTION_FAILED, errorCode, "");
    }

    public static ExecutionResult error(int errorCode, String errorMessage) {
        return new ExecutionResult(ResultTypeEnum.EXECUTION_FAILED, errorCode, errorMessage);
    }

    private final ResultTypeEnum resultType;
    private final int errorCode;
    private final String errorMessage;

    private ExecutionResult(ResultTypeEnum result, int errorCode, String errorMessage) {
        this.resultType = result;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ResultTypeEnum getResultType() {
        return resultType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}