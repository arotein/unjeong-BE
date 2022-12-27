package spharoom.unjeong.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import spharoom.unjeong.appointment.domain.entity.Appointment;
import spharoom.unjeong.appointment.domain.entity.Customer;
import spharoom.unjeong.appointment.domain.entity.PrivacyPolicy;
import spharoom.unjeong.global.common.CommonException;
import spharoom.unjeong.global.enumeration.AppointmentType;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAppointmentReqDto {
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private AppointmentType appointmentType;
    @NotNull
    private Integer numberOfPeople;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    @NotNull
    private Integer appointmentHour;
    @NotNull
    private Boolean personalInformationCollectionAndUsageAgreement;
    @NotNull
    private Boolean privacyPolicyRead;

    public Appointment toAppointmentEntity(Customer customer) {
        if (customer == null) {
            throw new CommonException(14, "Customer는 필수값입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (personalInformationCollectionAndUsageAgreement == false) {
            throw new CommonException(17, "개인정보 수집 및 이용 동의는 필수입니다.", HttpStatus.BAD_REQUEST);
        }
        if (privacyPolicyRead == false) {
            throw new CommonException(18, "개인정보 처리 방침을 읽어야합니다.", HttpStatus.BAD_REQUEST);
        }
        PrivacyPolicy privacyPolicy = PrivacyPolicy.builder()
                .personalInformationCollectionAndUsageAgreement(personalInformationCollectionAndUsageAgreement)
                .privacyPolicyRead(privacyPolicyRead)
                .build();

        return Appointment.builder()
                .appointmentType(appointmentType)
                .numberOfPeople(numberOfPeople)
                .appointmentDate(appointmentDate)
                .appointmentTime(LocalTime.of(appointmentHour, 0))
                .build()
                .mainLinkToCustomer(customer)
                .mainLinkToPrivacyPolicy(privacyPolicy);
    }

    public Customer toCustomerEntity() {
        return Customer.builder()
                .name(name)
                .phone(phone)
                .build();
    }

    public LocalTime getAppointmentTime() {
        return LocalTime.of(appointmentHour, 0);
    }

    public RequestAppointmentReqDto checkValidation() {
        LocalDate nowDate = LocalDate.now();
        LocalDate nextWeekDate = nowDate.plusDays(7);
        LocalTime nowTime = LocalTime.now();

        if (!name.matches("^[가-힣]+$")) {
            throw new CommonException(6, "이름은 한글만 가능합니다.");
        }
        if (!phone.matches("^01\\d-\\d{3,4}-\\d{4}$")) {
            throw new CommonException(7, "휴대폰 번호는 000-0000-0000의 형태만 가능합니다.");
        }
        if (numberOfPeople <= 0 || numberOfPeople >= 10) {
            throw new CommonException(8, "신청 인원수는 1~9명만 가능합니다.");
        }
        if (appointmentDate.isBefore(nowDate)) {
            throw new CommonException(16, "지난 날짜는 예약할 수 없습니다.");
        }
        if (appointmentDate.isAfter(nextWeekDate)) {
            throw new CommonException(9, "7일 이내만 예약이 가능합니다.");
        }
        if (appointmentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new CommonException(10, "일요일은 예약이 불가능합니다.");
        }
        if (appointmentHour < 11 || appointmentHour > 19) {
            throw new CommonException(11, "예약 가능 시간은 11~19시입니다.");
        }
        if (appointmentDate.isEqual(nowDate) && appointmentHour < nowTime.getHour()) {
            throw new CommonException(12, "지난 시간은 예약할 수 없습니다.");
        }
        if (appointmentDate.isEqual(nowDate) && appointmentHour == nowTime.getHour()) {
            throw new CommonException(13, String.format("%d시 이후 시간만 예약 가능합니다.", nowTime.getHour() + 1));
        }
        return this;
    }
}
