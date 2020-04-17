package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.service.constant.ExecutionResult;

public interface PasswordService {

    ExecutionResult changeUserPasswordByIdAndSendEmail(Long id);

}