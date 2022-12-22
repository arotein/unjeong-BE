package spharoom.unjeong.appointment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Comment;
import spharoom.unjeong.global.common.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Customer extends BaseEntity { // 300~
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;
    @Comment("예약자")
    @Column(nullable = false)
    private String name;
    @Comment("연락처")
    @Column(nullable = false)
    private String phone;
    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Appointment> appointmentList = new ArrayList<>();

    public void linkToAppointment(Appointment appointment) {
        appointmentList.add(appointment);
    }
}
