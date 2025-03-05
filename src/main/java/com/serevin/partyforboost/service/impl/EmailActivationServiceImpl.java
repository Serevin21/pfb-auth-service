package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.dto.email.ActivationRequest;
import com.serevin.partyforboost.dto.email.SendActivationEmailRequest;
import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.enums.UserStatus;
import com.serevin.partyforboost.event.EmailActivationEvent;
import com.serevin.partyforboost.exception.EntityNotFoundException;
import com.serevin.partyforboost.exception.ExceededAttemptsException;
import com.serevin.partyforboost.exception.InvalidActivationCodeException;
import com.serevin.partyforboost.gererator.CodeGenerator;
import com.serevin.partyforboost.service.EmailActivationService;
import com.serevin.partyforboost.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private int maxActivationEmailAttempts;
    private int maxFailedCodeEnteringAttempts;

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
        if (user.getActivationCodeSentTimes() >= maxActivationEmailAttempts) {
            LocalDateTime activationCodeLastSentAt = user.getActivationCodeLastSentAt();

            if (activationCodeLastSentAt != null && activationCodeLastSentAt.plusMinutes(5L).isAfter(LocalDateTime.now())) {
                throw new ExceededAttemptsException("You have exceeded activation email send attempts.", activationCodeLastSentAt.plusMinutes(5L));
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

        if (activationCodeEnteredTimes >= maxFailedCodeEnteringAttempts) {

            LocalDateTime invalidActivationCodeEnteredLastTimeAt = user.getInvalidActivationCodeEnteredLastTimeAt();
            if (invalidActivationCodeEnteredLastTimeAt != null &&
                    invalidActivationCodeEnteredLastTimeAt.plusMinutes(5L).isAfter(LocalDateTime.now())) {
                throw new ExceededAttemptsException("You have exceeded activation code entering attempts.",
                        invalidActivationCodeEnteredLastTimeAt.plusMinutes(5L));
            } else {
                user.setInvalidActivationCodeEnteredTimes(0);
            }
        }
    }

    @Value("${app.activation.email.maxActivationEmailAttempts}")
    public void setMaxActivationEmailAttempts(int maxActivationEmailAttempts) {
        this.maxActivationEmailAttempts = maxActivationEmailAttempts;
    }

    @Value("${app.activation.email.maxFailedCodeEnteringAttempts}")
    public void setMaxFailedCodeEnteringAttempts(int maxFailedCodeEnteringAttempts) {
        this.maxFailedCodeEnteringAttempts = maxFailedCodeEnteringAttempts;
    }
}
