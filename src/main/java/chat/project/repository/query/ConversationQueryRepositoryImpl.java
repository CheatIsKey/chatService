package chat.project.repository.query;

import chat.project.domain.Conversation;
import chat.project.domain.QConversation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConversationQueryRepositoryImpl implements ConversationQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<Conversation> searchConversations(String titleKeyword, LocalDateTime updatedFrom, Pageable pageable) {

        QConversation conv = QConversation.conversation;

        BooleanBuilder where = new BooleanBuilder();
        if (titleKeyword != null && !titleKeyword.isBlank()) {
            where.and(conv.title.containsIgnoreCase(titleKeyword));
        }
        if (updatedFrom != null) {
            where.and(conv.updatedAt.goe(updatedFrom));
        }

        List<Conversation> content = query
                .selectFrom(conv)
                .where(where)
                .orderBy(conv.updatedAt.desc(), conv.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(conv.count())
                .from(conv)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
