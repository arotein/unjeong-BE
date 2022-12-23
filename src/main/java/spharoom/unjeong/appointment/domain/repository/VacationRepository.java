package spharoom.unjeong.appointment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spharoom.unjeong.appointment.domain.entity.Vacation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    Optional<Vacation> findByVacationDate(LocalDate date);

    List<Vacation> findAllByVacationDateIsGreaterThanEqual(LocalDate date);

    void deleteByVacationDate(LocalDate date);

    boolean existsByVacationDate(LocalDate date);
}
