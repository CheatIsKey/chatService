package chat.project.repository;

import chat.project.domain.Conversation;
import chat.project.repository.query.ConversationQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long>, ConversationQueryRepository {

    Optional<Conversation> findByNickname(String nickname);
    boolean existsByIdAndCreatedBy(Long id, String userId);

}
