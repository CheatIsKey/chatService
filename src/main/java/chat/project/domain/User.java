package chat.project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    private User(String username, String password, String nickname, UserRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public static User of(String username, String encodedPassword, String nickname) {
        return new User(username, encodedPassword, nickname, UserRole.USER);
    }

    public void changeNickname(String newNickname) {
        if (newNickname == null || newNickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 반드시 입력되어야 합니다.");
        }

        String trimmed = newNickname.trim();

        if (trimmed.length() > 50) {
            throw new IllegalArgumentException("닉네임은 50자 이내로 지어야 합니다.");
        }

        if (trimmed.equals(this.nickname)) {
            return;
        }
        this.nickname = trimmed;
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 반드시 입력되어야 합니다.");
        }

        String trimmed = newPassword.trim();

        if (trimmed.length() < 20) {
            throw new IllegalArgumentException("입력된 비밀번호가 너무 짧습니다.");
        }

        if(trimmed.equals(this.password)) {
            return;
        }
        this.password = trimmed;
    }
}
