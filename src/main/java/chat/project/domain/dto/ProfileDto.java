package chat.project.domain.dto;

import chat.project.domain.User;

import java.time.LocalDateTime;

public record ProfileDto(
        String username,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProfileDto from(User user) {
        return new ProfileDto(user.getUsername(), user.getNickname(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
