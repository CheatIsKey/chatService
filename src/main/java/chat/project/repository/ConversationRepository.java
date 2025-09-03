package chat.project.repository;

import chat.project.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByTitle(String title);
    Optional<Conversation> findByNickname(String nickname);


}
