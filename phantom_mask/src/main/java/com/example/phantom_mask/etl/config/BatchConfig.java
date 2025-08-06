package com.example.phantom_mask.etl.config;

import com.example.phantom_mask.etl.model.ProcessedPharmacy;
import com.example.phantom_mask.etl.model.ProcessedUser;
import com.example.phantom_mask.etl.processor.PharmacyProcessor;
import com.example.phantom_mask.etl.processor.UserProcessor;
import com.example.phantom_mask.etl.reader.PharmacyItemReader;
import com.example.phantom_mask.etl.reader.UserItemReader;
import com.example.phantom_mask.etl.writer.PharmacyWriter;
import com.example.phantom_mask.etl.writer.UserWriter;
import com.example.phantom_mask.etl.model.Pharmacy;
import com.example.phantom_mask.etl.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Value("${etl.pharmacy.file}")
    private String pharmacyJsonPath;

    @Value("${etl.user.file}")
    private String userJsonPath;

    @Bean
    public PharmacyItemReader pharmacyItemReader() {
        return new PharmacyItemReader(pharmacyJsonPath);
    }

    public UserItemReader userItemReader() {
        return new UserItemReader(userJsonPath);
    }

    @Bean
    public PharmacyProcessor pharmacyProcessor() {
        return new PharmacyProcessor();
    }

    @Bean
    public UserProcessor userProcessor() {
        return new UserProcessor();
    }

    @Bean
    public PharmacyWriter pharmacyItemWriter(NamedParameterJdbcTemplate jdbcTemplate) {
        return new PharmacyWriter(jdbcTemplate);
    }

    @Bean
    public UserWriter userItemWriter(NamedParameterJdbcTemplate jdbcTemplate) {
        return new UserWriter(jdbcTemplate);
    }

    @Bean
    public Step pharmacyStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {
        return new StepBuilder("pharmacyStep", jobRepository)
            .<Pharmacy, ProcessedPharmacy>chunk(10, transactionManager)
            .reader(pharmacyItemReader())
            .processor(pharmacyProcessor())
            .writer(pharmacyItemWriter(null))
            .build();
    }

    @Bean
    public Step userStep(JobRepository repo, PlatformTransactionManager tx) {
        return new StepBuilder("userStep", repo)
            .<User, ProcessedUser>chunk(10, tx)
            .reader(userItemReader())
            .processor(userProcessor())
            .writer(userItemWriter(null))
            .build();
    }

    @Bean
    public Job etlJob(JobRepository jobRepository, Step pharmacyStep, Step userStep) {
        return new JobBuilder("etlJob", jobRepository)
            .start(pharmacyStep)
            .next(userStep)
            .build();
    }
}