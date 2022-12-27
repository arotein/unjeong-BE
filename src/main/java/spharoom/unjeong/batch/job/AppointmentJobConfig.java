package spharoom.unjeong.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spharoom.unjeong.appointment.domain.entity.Customer;
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;
import spharoom.unjeong.appointment.domain.repository.CustomerRepository;
import spharoom.unjeong.appointment.domain.repository.VacationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class AppointmentJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AppointmentRepository appointmentRepository;
    private final VacationRepository vacationRepository;
    private final CustomerRepository customerRepository;

    @Bean
    public Job appointmentStateJob() {
        return jobBuilderFactory.get("appointmentStateJob")
                .start(toDoneStep()).on("*")
                .to(deleteAppointmentStep()).on("*")
                .to(deleteVacationStep())
                .end()
                .build();
    }

    @Bean
    public Job deletePersonalInformationJob() {
        return jobBuilderFactory.get("deletePersonalInformationJob")
                .start(deletePersonalInformationStep())
                .build();
    }

    @Bean
    public Step toDoneStep() {
        return stepBuilderFactory.get("toDoneStep")
                .tasklet((contribution, chunkContext) -> {
                    LocalDate yesterday = LocalDate.now().minusDays(1L);
                    appointmentRepository.stateToDoneOfDateWithBatch(yesterday);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step deleteAppointmentStep() {
        return stepBuilderFactory.get("deleteAppointmentStep")
                .tasklet((contribution, chunkContext) -> {
                    LocalDate yesterday = LocalDate.now().minusDays(1L);
                    appointmentRepository.deleteAppointmentOfDateWithBatch(yesterday);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step deleteVacationStep() {
        return stepBuilderFactory.get("deleteVacationStep")
                .tasklet((contribution, chunkContext) -> {
                    LocalDate yesterday = LocalDate.now().minusDays(1L);
                    vacationRepository.deleteByVacationDate(yesterday);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step deletePersonalInformationStep() {
        return stepBuilderFactory.get("deletePersonalInformationStep")
                .tasklet((contribution, chunkContext) -> {
                    LocalDateTime lastYear = LocalDateTime.now().minusYears(1);
                    List<Customer> customerList = customerRepository.findAllByLastAppointmentRequestDateTimeIsBefore(lastYear);
                    customerList.forEach(Customer::deletePersonalInformation);
                    return RepeatStatus.FINISHED;
                }).build();
    }
}