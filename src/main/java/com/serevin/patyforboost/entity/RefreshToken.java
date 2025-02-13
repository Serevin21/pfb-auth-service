package com.serevin.patyforboost.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "refresh_token")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiry_at", nullable = false)
    private LocalDateTime expiryAt;

    @Column(name = "device_agent", nullable = true)
    private String deviceAgent;
}
