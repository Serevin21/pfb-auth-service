package com.serevin.partyforboost.service;

import com.serevin.partyforboost.dto.reset.password.ChangePasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ResetPasswordRequest;
import com.serevin.partyforboost.dto.reset.password.ValidateCodeRequest;

public interface ResetPasswordService {

    void resetPassword(ResetPasswordRequest request);
    void validateCode(ValidateCodeRequest request);
    void changePassword(ChangePasswordRequest request);

}
