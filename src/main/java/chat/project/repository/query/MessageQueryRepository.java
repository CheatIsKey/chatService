package chat.project.repository.query;

import chat.project.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MessageQueryRepository {

    Page<Message> findMessageByConversation(Long conversationId, Pageable pageable);
    Page<Message> searchMessages(Long conversationId, Long afterMessageId, LocalDateTime from,
                                 String keyword, Pageable pageable);
    Page<Message> searchMessagesByKeyword(Long conversationId, String keyword, Pageable pageable);
}
