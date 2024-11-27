package com.openclassrooms.mddapi.domains.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "L'email ne doit pas être vide")
        @Email(message = "L'email doit avoir un format valide")
        @Size(max = 254, message = "L'email doit être inférieur à 254 caractères")
        String email,

        @NotBlank(message = "Le nom d'utilisateur ne doit pas être vide")
        @Size(max = 50, message = "Le nom d'utilisateur doit être inférieur à 50 caractères")
        @Pattern(regexp = "^\\w*$", message = "Le nom d'utilisateur ne peut contenir que des caractères alphanumériques")
        String username,

        @NotBlank(message = "Le mot de passe ne doit pas être vide")
        @Size(min = 8, max = 64, message = "Le mot de passe doit comprendre entre 8 et 64 caractères")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$", message = "Le mot de passe doit contenir au moins un chiffre, une lettre minuscule, une lettre majuscule et un caractère spécial")
        String password
) {
}
