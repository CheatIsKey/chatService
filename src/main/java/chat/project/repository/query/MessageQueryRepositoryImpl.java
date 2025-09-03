package chat.project.repository.query;

import chat.project.domain.Message;
import chat.project.domain.QMessage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static chat.project.domain.QConversation.conversation;
import static chat.project.domain.QMessage.*;

@Repository
@RequiredArgsConstructor
public class MessageQueryRepositoryImpl implements MessageQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<Message> findMessageByConversation(Long conversationId, Pageable pageable) {

        List<Message> content = query
                .selectFrom(message)
                .where(message.conversation.id.eq(conversationId))
                .orderBy(message.createdAt.asc(), message.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(message.count())
                .from(message)
                .where(message.conversation.id.eq(conversationId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<Message> searchMessages(Long conversationId, Long afterMessageId, LocalDateTime from, String keyword, Pageable pageable) {

        BooleanBuilder where = new BooleanBuilder()
                .and(message.conversation.id.eq(conversationId));

        if (afterMessageId != null) {
            where.and(message.id.gt(afterMessageId));
        }
        if (from != null) {
            where.and(message.createdAt.goe(from));
        }
        if (keyword != null && !keyword.isBlank()) {
            where.and(message.content.containsIgnoreCase(keyword));
        }

        List<Message> content = query
                .selectFrom(message)
                .where(where)
                .orderBy(message.createdAt.asc(), message.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(message.count())
                .from(message)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<Message> searchMessagesByKeyword(Long conversationId, String keyword, Pageable pageable) {

        BooleanBuilder where = new BooleanBuilder()
                .and(message.conversation.id.eq(conversationId));

        if (keyword != null && !keyword.isBlank()) {
            where.and(message.content.containsIgnoreCase(keyword));
        }

        List<Message> content = query
                .selectFrom(message)
                .where(where)
                .orderBy(message.createdAt.desc(), message.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(message.count())
                .from(message)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
