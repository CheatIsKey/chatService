package chat.project.domain.dto;

import chat.project.domain.User;

import java.time.LocalDateTime;

public record UserDto(String username, String nickname, LocalDateTime createdAt) {
    public static UserDto from(User user) {
        return new UserDto(user.getUsername(), user.getNickname(), user.getCreatedAt());
    }
}
