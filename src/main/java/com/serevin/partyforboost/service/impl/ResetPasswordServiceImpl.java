package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.dto.reset.password.ChangePasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ResetPasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ValidateCodeRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.event.EmailResetPasswordEvent;
import com.serevin.partyforboost.exception.ExceededAttemptsException;
import com.serevin.partyforboost.exception.InvalidResetPasswordCodeException;
import com.serevin.partyforboost.gererator.CodeGenerator;
import com.serevin.partyforboost.service.ResetPasswordService;
import com.serevin.partyforboost.service.UserService;
import com.serevin.partyforboost.utils.ResetPasswordsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserService userService;
    private final CodeGenerator codeGenerator;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.email();
        User user = userService.getByEmail(email);
        validateResetPassword(user);

        String code = codeGenerator.generate();
        user.setResetPasswordCode(code);
        user.setResetPasswordCodeLastSentAt(LocalDateTime.now());
        int resetPasswordSentTimes = user.getResetPasswordSentTimes();
        user.setResetPasswordSentTimes(++resetPasswordSentTimes);
        userService.save(user);

        publisher.publishEvent(new EmailResetPasswordEvent(this, email, code));
    }

    private void validateResetPassword(User user) {
        if (user.getResetPasswordSentTimes() >= ResetPasswordsProperties.MAX_RESET_PASSWORD_ATTEMPTS) {

            LocalDateTime resetPasswordCodeLastSentAt = user.getResetPasswordCodeLastSentAt();
            if (resetPasswordCodeLastSentAt != null && resetPasswordCodeLastSentAt
                    .plusMinutes(ResetPasswordsProperties.RESET_PASSWORD_CODE_EXPIRATION_MINUTES).isAfter(LocalDateTime.now())) {
                throw new ExceededAttemptsException("You have exceeded reset password attempts.", resetPasswordCodeLastSentAt
                        .plusMinutes(ResetPasswordsProperties.RESET_PASSWORD_CODE_EXPIRATION_MINUTES));
            } else {
                user.setResetPasswordSentTimes(0);
            }
        }
    }

    @Override
    public void validateCode(ValidateCodeRequest request) {
        User user = userService.getByEmail(request.email());
        validateChangePassword(user);

        if (request.code().equals(user.getResetPasswordCode())) {
            user.setInvalidResetPasswordCodeEnteredTimes(0);
            user.setInvalidResetPasswordCodeEnteredLastTimeAt(null);
            userService.save(user);
            return;
        } else {
            int resetPasswordCodeEnteredTimes = user.getInvalidResetPasswordCodeEnteredTimes() + 1;
            user.setInvalidResetPasswordCodeEnteredTimes(resetPasswordCodeEnteredTimes);
            user.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now());
            userService.save(user);
        }
        throw new InvalidResetPasswordCodeException("You have entered an invalid reset password code");
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = userService.getByEmail(request.email());
        validateChangePassword(user);

        if (request.code().equals(user.getResetPasswordCode())) {
            user.setResetPasswordCode(null);
            user.setResetPasswordCodeLastSentAt(null);
            user.setResetPasswordSentTimes(0);
            user.setInvalidResetPasswordCodeEnteredTimes(0);
            user.setInvalidResetPasswordCodeEnteredLastTimeAt(null);
            String newPassword = passwordEncoder.encode(request.password());
            user.setPassword(newPassword);
            userService.save(user);
            return;
        } else {
            int resetPasswordCodeEnteredTimes = user.getInvalidResetPasswordCodeEnteredTimes() + 1;
            user.setInvalidResetPasswordCodeEnteredTimes(resetPasswordCodeEnteredTimes);
            user.setInvalidResetPasswordCodeEnteredLastTimeAt(LocalDateTime.now());
            userService.save(user);
        }
        throw new InvalidResetPasswordCodeException("You have entered an invalid reset password code");
    }

    private void validateChangePassword(User user) {
        int invalidResetPasswordCodeEnteredTimes = user.getInvalidResetPasswordCodeEnteredTimes() + 1;

        if (invalidResetPasswordCodeEnteredTimes >= ResetPasswordsProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS) {

            LocalDateTime invalidResetPasswordCodeEnteredLastTimeAt = user.getInvalidResetPasswordCodeEnteredLastTimeAt();
            if (invalidResetPasswordCodeEnteredLastTimeAt != null &&
                    invalidResetPasswordCodeEnteredLastTimeAt.plusMinutes(ResetPasswordsProperties.FAILED_CODE_ENTERING_LOCK_DURATION_MINUTES)
                            .isAfter(LocalDateTime.now())) {
                throw new ExceededAttemptsException("You have exceeded reset password code entering attempts.",
                        invalidResetPasswordCodeEnteredLastTimeAt.plusMinutes(ResetPasswordsProperties.FAILED_CODE_ENTERING_LOCK_DURATION_MINUTES));
            } else {
                user.setInvalidResetPasswordCodeEnteredTimes(0);
            }
        }
    }

}
