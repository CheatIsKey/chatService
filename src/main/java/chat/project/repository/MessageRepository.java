package chat.project.repository;

import chat.project.domain.Message;
import chat.project.repository.query.MessageQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageQueryRepository {



}
