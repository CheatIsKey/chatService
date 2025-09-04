package chat.project.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size String username,
        @NotBlank String password,
        @Size(max = 50) String nickname
) {}
