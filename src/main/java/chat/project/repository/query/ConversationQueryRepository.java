package chat.project.repository.query;

import chat.project.domain.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ConversationQueryRepository {

    Page<Conversation> searchConversations(String titleKeyword, LocalDateTime updatedForm, Pageable pageable);

}
