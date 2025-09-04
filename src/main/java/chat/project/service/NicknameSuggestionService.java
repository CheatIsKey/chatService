package chat.project.service;

import chat.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NicknameSuggestionService {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    /**
     *
     * @param base 기본 닉네임
     * @param count 후보의 개수
     * @return 닉네임 후보 리스트
     */

    public List<String> suggest(String base, int count) {
        String normalized = normalize(base);
        int target = Math.max(1, Math.min(count, 20));
        int attempts = 0;
        Set<String> results = new LinkedHashSet<>();

        while (results.size() < target && attempts < target * 15) {
            int digits = 1 + random.nextInt(5);
            long num = nextNumberWithDigits(digits);
            String candidate = normalized + "_" + num;
            candidate = clampLength(candidate, 50);

            if (!userRepository.existsByNickname(candidate)) {
                results.add(candidate);
            }
            attempts++;
        }
        return List.copyOf(results);
    }

    private String normalize(String base) {
        if (base == null)
            return "user";

        String s = base.trim();
        if (s.isEmpty())
            return "user";

        return s;
    }

    private long nextNumberWithDigits(int digits) {
        long lower = (long) Math.pow(10, digits - 1);
        long upper = (long) Math.pow(10, digits) - 1;
        return lower + (Math.abs(random.nextLong()) % (upper - lower + 1));
    }

    private String clampLength(String candidate, int max) {
        return candidate.length() <= max ? candidate : candidate.substring(0, max);
    }
}
