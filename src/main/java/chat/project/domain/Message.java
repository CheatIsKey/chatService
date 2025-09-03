package chat.project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "messages",
        indexes = {
                @Index(name = "idx_msg_conv_created", columnList = "conversation_id, createdAt")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @Lob @Column(nullable = false)
    private String content;

    @JoinColumn(name = "conversation_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Conversation conversation;

    @JoinColumn(name = "sender_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User sender;


}
