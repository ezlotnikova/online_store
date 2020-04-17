package com.gmail.ezlotnikova.service.impl;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.service.PasswordService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.mail.MailService;
import com.gmail.ezlotnikova.service.util.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.ezlotnikova.service.mail.ChangePasswordMessageConstant.CHANGE_PASSWORD_EMAIL_SUBJECT;
import static com.gmail.ezlotnikova.service.mail.ChangePasswordMessageConstant.CHANGE_PASSWORD_EMAIL_TEMPLATE;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordGenerator passwordGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    public PasswordServiceImpl(
            PasswordGenerator passwordGenerator, BCryptPasswordEncoder bCryptPasswordEncoder,
            UserRepository userRepository, MailService mailService) {
        this.passwordGenerator = passwordGenerator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public ExecutionResult changeUserPasswordByIdAndSendEmail(Long id) {
        String password = passwordGenerator.generatePassword();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user = userRepository.findById(id);
        String emailAddress = user.getEmail();
        user.setPassword(encryptedPassword);
        userRepository.merge(user);
        sendNewPasswordToUser(emailAddress, password);
        return ExecutionResult.ok();
    }

    private void sendNewPasswordToUser(String emailAddress, String password) {
        String emailContent = String.format(CHANGE_PASSWORD_EMAIL_TEMPLATE, password);
        mailService.sendMessage(
                emailAddress,
                CHANGE_PASSWORD_EMAIL_SUBJECT,
                emailContent);
    }

}