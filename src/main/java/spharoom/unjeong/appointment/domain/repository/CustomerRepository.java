package spharoom.unjeong.appointment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spharoom.unjeong.appointment.domain.entity.Customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByNameAndPhone(String name, String phone);

    List<Customer> findAllByLastAppointmentRequestDateTimeIsBefore(LocalDateTime lastYear);
}
