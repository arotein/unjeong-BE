package spharoom.unjeong.appointment.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;
import spharoom.unjeong.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Admin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Comment("로그인아이디")
    @Column(nullable = false, unique = true)
    private String loginId;

    @Comment("로그인비밀번호")
    @Column(nullable = false)
    private String password;

    @Comment("마지막접속일")
    @Column(nullable = false, updatable = false)
    private LocalDateTime lastLoginDateTime;
}
