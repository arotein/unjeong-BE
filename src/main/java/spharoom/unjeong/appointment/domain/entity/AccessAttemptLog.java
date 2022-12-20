package spharoom.unjeong.appointment.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;
import spharoom.unjeong.global.enumeration.AccessResult;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AccessAttemptLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_attempt_log_id")
    private Long id;

    @Comment("접근시도시간")
    @Column(nullable = false, updatable = false)
    private LocalDateTime attemptDateTime;

    @Comment("요청IP주소")
    @Column(nullable = false, updatable = false)
    private String ipAddress;

    @Comment("접근결과")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AccessResult accessResult;
}
