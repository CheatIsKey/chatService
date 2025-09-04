package chat.project.service;

import chat.project.domain.User;
import chat.project.domain.UserCreateRequest;
import chat.project.domain.condition.UserCondition;
import chat.project.domain.dto.ProfileDto;
import chat.project.domain.dto.UpdateProfileRequest;
import chat.project.domain.dto.UserDto;
import chat.project.exception.DuplicateNicknameException;
import chat.project.exception.DuplicateUsernameException;
import chat.project.exception.NicknameMissingException;
import chat.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final NicknameSuggestionService nicknameSuggestionService;

    /**
     *  계정 생성
     */
    public Long register(UserCreateRequest request) {

        final String username = request.username().trim();
        final String rawPassword = request.password().trim();
        final String nickname = safeTrim(request.nickname());

        if (nickname == null || nickname.isBlank()) {
            throw new NicknameMissingException();
        }
        if (repository.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
        if (repository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException(nickname);
        }

        final String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = User.of(username, encodedPassword, nickname);

        try {
            repository.save(user);
            return user.getId();
        } catch (DataIntegrityViolationException e) {
            if (repository.existsByUsername(username)) {
                throw new DuplicateUsernameException(username);
            }
            if (repository.existsByNickname(nickname)) {
                throw new DuplicateNicknameException(nickname);
            }
            throw e;
        }
    }

    /**
     *  닉네임 추천
     */
    @Transactional(readOnly = true)
    public List<String> suggestNicknames(String base, int count) {
        return nicknameSuggestionService.suggest(base, count);
    }

    /**
     *  사용자 찾기 (단일 결과)
     */
    @Transactional(readOnly = true)
    public UserDto findUser(UserCondition condition) {

        final String username = safeTrim(condition.username());
        final String nickname = safeTrim(condition.nickname());

        if ((username == null || username.isBlank()) &&
                (nickname == null || nickname.isBlank())) {
            throw new IllegalArgumentException("username 또는 nickname 중 하나는 필수입니다.");
        }

        if (username != null && !username.isBlank()) {
            var user = repository.findByUsername(username)
                    .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
            return UserDto.from(user);
        }

        var user = repository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        return UserDto.from(user);
    }

    @Transactional(readOnly = true)
    public ProfileDto getMyProfile(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자가 없습니다."));
        return ProfileDto.from(user);
    }

    public ProfileDto updateMyProfile(Long userId, UpdateProfileRequest request, String currentPasswordOrNull) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자가 없습니다."));

        String newNickname = safeTrim(request.nickname());
        if (newNickname != null && !newNickname.isBlank()) {
            if (!newNickname.equals(user.getNickname())) {
                if (repository.existsByNickname(newNickname)) {
                    throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
                }
                user.changeNickname(newNickname);
            }
        }

        String newRawPassword = request.newPassword();
        if (newRawPassword != null && !newRawPassword.isBlank()) {
            if (currentPasswordOrNull != null) {
                if (!passwordEncoder.matches(currentPasswordOrNull, user.getPassword())) {
                    throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
                }
            }
            validatePasswordPolicy(newRawPassword);

            if (passwordEncoder.matches(newRawPassword, user.getPassword())) {
                throw new IllegalArgumentException("이전과 동일한 비밀번호는 사용할 수 없습니다.");
            }

            String encoded = passwordEncoder.encode(newRawPassword);
            user.changePassword(encoded);
        }
        return ProfileDto.from(user);
    }

    private String safeTrim(String nickname) {
        return nickname == null ? null : nickname.trim();
    }

    private void validatePasswordPolicy(String rawPassword) {
        int len = rawPassword.length();

        if (len < 8 || len > 20) {
            throw new IllegalArgumentException("비밀번호는 8 ~ 20자여야 합니다.");
        }
        if (rawPassword.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("비밀번호에 공백은 사용할 수 없습니다.");
        }
    }

}
