package chat.project.repository.query;

import chat.project.domain.Message;
import chat.project.domain.QMessage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static chat.project.domain.QConversation.conversation;
import static chat.project.domain.QMessage.*;

@Repository
@RequiredArgsConstructor
public class MessageQueryRepositoryImpl implements MessageQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<Message> searchMessages(Long ConversationId, Long afterMessageId, LocalDateTime from, String keyword, Pageable pageable) {

        BooleanBuilder where = new BooleanBuilder()
                .and(message.conversation.id.eq(conversation.id));

        
    }
}
