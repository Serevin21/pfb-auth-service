package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.dto.email.ActivationRequest;
import com.serevin.partyforboost.dto.email.SendActivationEmailRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.enums.UserStatus;
import com.serevin.partyforboost.event.EmailActivationEvent;
import com.serevin.partyforboost.exception.ExceededAttemptsException;
import com.serevin.partyforboost.exception.InvalidActivationCodeException;
import com.serevin.partyforboost.gererator.CodeGenerator;
import com.serevin.partyforboost.service.EmailActivationService;
import com.serevin.partyforboost.service.UserService;
import com.serevin.partyforboost.utils.EmailActivationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailActivationServiceImpl implements EmailActivationService {

    private final CodeGenerator codeGenerator;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public void sendEmail(SendActivationEmailRequest request) {
        String email = request.email();
        User user = userService.getByEmail(email);
        validateActivationEmail(user);

        String code = codeGenerator.generate();
        user.setActivationCode(code);
        int activationCodeSentTimes = user.getActivationCodeSentTimes();
        user.setActivationCodeSentTimes(++activationCodeSentTimes);
        user.setActivationCodeLastSentAt(LocalDateTime.now());

        userService.save(user);
        publisher.publishEvent(new EmailActivationEvent(this, email, code));
    }

    private void validateActivationEmail(User user) {
        int maxActivationEmailAttempts = EmailActivationProperties.MAX_ACTIVATION_EMAIL_ATTEMPTS;
        if (user.getActivationCodeSentTimes() >= maxActivationEmailAttempts) {
            LocalDateTime activationCodeLastSentAt = user.getActivationCodeLastSentAt();

            if (activationCodeLastSentAt != null &&
                    activationCodeLastSentAt.plusMinutes(EmailActivationProperties.ACTIVATION_EMAIL_COOLDOWN_MINUTES).isAfter(LocalDateTime.now())) {
                throw new ExceededAttemptsException("You have exceeded activation email send attempts.",
                        activationCodeLastSentAt.plusMinutes(EmailActivationProperties.ACTIVATION_EMAIL_COOLDOWN_MINUTES));
            } else {
                user.setActivationCodeSentTimes(0);
            }
        }
    }

    @Transactional(noRollbackFor = {InvalidActivationCodeException.class})
    @Override
    public void activate(ActivationRequest request) {
        User user = userService.getByEmail(request.email());
        validateConfirmEmail(user);

        if (request.code().equals(user.getActivationCode())) {
            user.setActivationCode(null);
            user.setActivationCodeSentTimes(0);
            user.setActivationCodeLastSentAt(null);
            user.setStatus(UserStatus.ACTIVE);
            user.setInvalidActivationCodeEnteredTimes(0);
            user.setInvalidActivationCodeEnteredLastTimeAt(null);
            userService.save(user);
            return;
        }
        int activationCodeEnteredTimes = user.getInvalidActivationCodeEnteredTimes() + 1;
        user.setInvalidActivationCodeEnteredTimes(activationCodeEnteredTimes);
        user.setInvalidActivationCodeEnteredLastTimeAt(LocalDateTime.now());
        userService.save(user);
        throw new InvalidActivationCodeException("You have entered an invalid activation code");
    }

    private void validateConfirmEmail(User user) {
        int activationCodeEnteredTimes = user.getInvalidActivationCodeEnteredTimes() + 1;

        int maxFailedCodeEnteringAttempts = EmailActivationProperties.MAX_FAILED_CODE_ENTERING_ATTEMPTS;
        if (activationCodeEnteredTimes >= maxFailedCodeEnteringAttempts) {

            LocalDateTime invalidActivationCodeEnteredLastTimeAt = user.getInvalidActivationCodeEnteredLastTimeAt();
            if (invalidActivationCodeEnteredLastTimeAt != null &&
                    invalidActivationCodeEnteredLastTimeAt.plusMinutes(EmailActivationProperties.INVALID_CODE_COOLDOWN_MINUTES)
                            .isAfter(LocalDateTime.now())) {
                throw new ExceededAttemptsException("You have exceeded activation code entering attempts.",
                        invalidActivationCodeEnteredLastTimeAt.plusMinutes(EmailActivationProperties.INVALID_CODE_COOLDOWN_MINUTES));
            } else {
                user.setInvalidActivationCodeEnteredTimes(0);
            }
        }
    }
}
