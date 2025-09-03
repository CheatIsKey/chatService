package chat.project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "conversations",
        indexes = {
                @Index(name = "idx_conv_updated_at", columnList = "updatedAt")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Conversation extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "conversation_id")
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @JoinColumn(name = "created_by_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User createdBy;





}
