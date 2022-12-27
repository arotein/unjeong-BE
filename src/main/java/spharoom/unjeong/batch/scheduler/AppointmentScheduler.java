package spharoom.unjeong.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentScheduler {
    private final JobLauncher jobLauncher; // job을 실행시키기 위한 런쳐. parameter를 이용하여 각 job들을 구분함
    private final Job appointmentStateJob;
    private final Job deletePersonalInformationJob;

    @Scheduled(cron = "0 1 0 * * *") // 매일 0시 1분에 실행
    public void appointmentStateJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter> jobParameterMap = new HashMap<>();
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        jobParameterMap.put("jobDateTime", new JobParameter(dateTime));
        jobLauncher.run(appointmentStateJob, new JobParameters(jobParameterMap));
    }

    @Scheduled(cron = "0 2 0 * * *") // 매일 0시 2분에 실행
    public void deletePersonalInformationJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter> jobParameterMap = new HashMap<>();
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        jobParameterMap.put("jobDateTime", new JobParameter(dateTime));
        jobLauncher.run(deletePersonalInformationJob, new JobParameters(jobParameterMap));
    }
}
