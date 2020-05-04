package com.gmail.ezlotnikova.service.constant;

import java.util.Objects;

public class ExecutionResult {

    public static ExecutionResult ok() {
        return new ExecutionResult(
                ResultTypeEnum.EXECUTED_SUCCESSFULLY, ErrorCodeConstant.NO_ERROR, null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecutionResult result = (ExecutionResult) o;
        return errorCode == result.errorCode &&
                resultType == result.resultType &&
                Objects.equals(errorMessage, result.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultType, errorCode, errorMessage);
    }

}