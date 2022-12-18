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
import spharoom.unjeong.appointment.domain.repository.AppointmentRepository;

import java.time.LocalDate;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class AppointmentJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AppointmentRepository appointmentRepository;

    @Bean
    public Job appointmentStateJob() {
        return jobBuilderFactory.get("appointmentStateJob")
                .start(toDoneStep()).on("*")
                .to(deleteAppointmentStep())
                .end()
                .build();
    }

    @Bean
    public Step toDoneStep() {
        return stepBuilderFactory.get("toDoneStep")
                .tasklet((contribution, chunkContext) -> {
                    LocalDate yesterday = LocalDate.now().minusDays(1L);
                    appointmentRepository.stateToDoneOfDateWithBatch(yesterday);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step deleteAppointmentStep() {
        return stepBuilderFactory.get("deleteAppointmentStep")
                .tasklet((contribution, chunkContext) -> {
                    LocalDate yesterday = LocalDate.now().minusDays(1L);
                    appointmentRepository.deleteAppointmentOfDateWithBatch(yesterday);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
