package chat.project.domain.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 50, message = "닉네임은 50자 이내여야 합니다.")
        String nickname,

        @Size(min = 8, max = 20, message = "비밀번호는 8 ~ 20자여야 합니다.")
        @Pattern(regexp = "^\\S+$", message = "비밀번호에 공백은 사용할 수 없습니다.")
        String newPassword
) {}
