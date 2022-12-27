package spharoom.unjeong.appointment.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;
import spharoom.unjeong.global.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PrivacyPolicy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_policy_id")
    private Long id;
    @Comment("개인정보수집이용동의여부") // 예약관련 안내 시에만 사용
    @Column(nullable = false, updatable = false)
    private Boolean personalInformationCollectionAndUsageAgreement;

    @Comment("개인정보처리방침읽음여부") // 이름, 전화번호는 마지막으로 예약한 날로부터 1년간 저장, 이후엔 삭제처리
    @Column(nullable = false, updatable = false)
    private Boolean privacyPolicyRead;

    @OneToOne(mappedBy = "privacyPolicy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Appointment appointment;

    public void linkToAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
