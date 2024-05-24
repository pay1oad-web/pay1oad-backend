package com.pay1oad.homepage.model.login;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {
    @Id
    @GeneratedValue(generator = "UUID_GENERATOR")
    @GenericGenerator(
            name="UUID_GENERATOR",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String verificationID;

    private String username;
}
