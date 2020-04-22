package com.gmail.ezlotnikova.service.impl;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.service.PasswordService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.mail.MailService;
import com.gmail.ezlotnikova.service.util.password.RandomPasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.ezlotnikova.service.mail.ChangePasswordMessageConstant.CHANGE_PASSWORD_EMAIL_SUBJECT;
import static com.gmail.ezlotnikova.service.mail.ChangePasswordMessageConstant.CHANGE_PASSWORD_EMAIL_TEMPLATE;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final RandomPasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    public PasswordServiceImpl(
            RandomPasswordGenerator passwordGenerator,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            MailService mailService
    ) {
        this.passwordGenerator = passwordGenerator;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public ExecutionResult generatePasswordAndSendEmail(Long id) {
        String newPassword = passwordGenerator.generateRandomPassword();
        changePasswordByUserId(id, newPassword);
        sendNewPasswordToUser(id, newPassword);
        return ExecutionResult.ok();
    }

    @Transactional
    @Override
    public ExecutionResult changePasswordByUserId(Long id, String newPassword){
        String encryptedPassword = passwordEncoder.encode(newPassword);
        User user = userRepository.findById(id);
        user.setPassword(encryptedPassword);
        userRepository.merge(user);
        return ExecutionResult.ok();
    }

    private void sendNewPasswordToUser(Long userId, String password) {
        User user = userRepository.findById(userId);
        String emailAddress = user.getEmail();
        String emailContent = String.format(CHANGE_PASSWORD_EMAIL_TEMPLATE, password);
        mailService.sendMessage(
                emailAddress,
                CHANGE_PASSWORD_EMAIL_SUBJECT,
                emailContent);
    }

}