package com.example.phantom_mask.etl.config;

import com.example.phantom_mask.etl.model.ProcessedPharmacy;
import com.example.phantom_mask.etl.processor.PharmacyProcessor;
import com.example.phantom_mask.etl.reader.PharmacyItemReader;
import com.example.phantom_mask.etl.writer.PharmacyWriter;
import com.example.phantom_mask.model.Pharmacy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Value("${etl.pharmacy.file}")
    private String pharmacyJsonPath;

    @Bean
    public PharmacyItemReader pharmacyItemReader() {
        return new PharmacyItemReader(pharmacyJsonPath);
    }

    @Bean
    public PharmacyProcessor pharmacyProcessor() {
        return new PharmacyProcessor();
    }

    @Bean
    public PharmacyWriter pharmacyItemWriter(NamedParameterJdbcTemplate jdbcTemplate) {
        return new PharmacyWriter(jdbcTemplate);
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
    public Job etlJob(JobRepository jobRepository, Step pharmacyStep) {
        return new JobBuilder("etlJob", jobRepository)
            .start(pharmacyStep)
            .build();
    }
}