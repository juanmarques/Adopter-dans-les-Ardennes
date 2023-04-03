package com.jme.adopterdla.adopterdla.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    private Long id;
    @NotBlank
    private String name;
    private String imageUrl;
    private String address;
    private String phone;
    @Email
    private String email;
}
