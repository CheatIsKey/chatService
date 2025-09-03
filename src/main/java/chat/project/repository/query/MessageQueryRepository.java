package chat.project.repository.query;

import chat.project.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MessageQueryRepository {

    Page<Message> searchMessages(Long ConversationId, Long afterMessageId, LocalDateTime from,
                                 String keyword, Pageable pageable);
}
