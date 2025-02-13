package com.serevin.patyforboost.entity;

import com.serevin.patyforboost.enums.Role;
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
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.NOVICE;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "activation_code_last_sent_at")
    private LocalDateTime activationCodeLastSentAt;

    @Column(name = "activation_code_sent_times")
    private Integer activationCodeSentTimes;

    @Column(name = "invalid_activation_code_entered_times")
    private Integer invalidActivationCodeEnteredTimes;

    @Column(name = "invalid_activation_code_entered_last_time_at")
    private LocalDateTime invalidActivationCodeEnteredLastTimeAt;

    @Column(name = "reset_password_code")
    private String resetPasswordCode;

    @Column(name = "reset_password_code_last_sent_at")
    private LocalDateTime resetPasswordCodeLastSentAt;

    @Column(name = "reset_password_sent_times")
    private Integer resetPasswordSentTimes;

    @Column(name = "invalid_reset_password_code_entered_times")
    private Integer invalidResetPasswordCodeEnteredTimes;

    @Column(name = "invalid_reset_password_code_entered_last_time_at")
    private LocalDateTime invalidResetPasswordCodeEnteredLastTimeAt;

}
