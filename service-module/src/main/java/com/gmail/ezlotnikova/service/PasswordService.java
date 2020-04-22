package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.service.constant.ExecutionResult;

public interface PasswordService {

    ExecutionResult generatePasswordAndSendEmail(Long userId);

    ExecutionResult changePasswordByUserId(Long id, String newPassword);

}