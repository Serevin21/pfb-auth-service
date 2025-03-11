package com.serevin.partyforboost.entity;

import com.serevin.partyforboost.enums.Role;
import com.serevin.partyforboost.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Builder.Default
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus status = UserStatus.UNVERIFIED;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.NOVICE;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "activation_code_last_sent_at")
    private LocalDateTime activationCodeLastSentAt;

    @Column(name = "activation_code_sent_times", nullable = false)
    @Builder.Default
    private Integer activationCodeSentTimes = 0;

    @Column(name = "invalid_activation_code_entered_times", nullable = false)
    @Builder.Default
    private Integer invalidActivationCodeEnteredTimes = 0;

    @Column(name = "invalid_activation_code_entered_last_time_at")
    private LocalDateTime invalidActivationCodeEnteredLastTimeAt;

    @Column(name = "reset_password_code")
    private String resetPasswordCode;

    @Column(name = "reset_password_code_last_sent_at")
    private LocalDateTime resetPasswordCodeLastSentAt;

    @Column(name = "reset_password_sent_times", nullable = false)
    @Builder.Default
    private Integer resetPasswordSentTimes = 0;

    @Column(name = "invalid_reset_password_code_entered_times", nullable = false)
    @Builder.Default
    private Integer invalidResetPasswordCodeEnteredTimes = 0;

    @Column(name = "invalid_reset_password_code_entered_last_time_at")
    private LocalDateTime invalidResetPasswordCodeEnteredLastTimeAt;

    @Column(name = "invalid_password_entered_times", nullable = false)
    @Builder.Default
    private Integer invalidPasswordEnteredTimes = 0;

    @Column(name = "invalid_password_entered_last_time_at")
    private LocalDateTime invalidPasswordEnteredLastTimeAt;

}
