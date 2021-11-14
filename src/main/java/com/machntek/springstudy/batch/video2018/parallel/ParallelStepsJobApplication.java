package com.machntek.springstudy.batch.video2018.parallel;

import com.machntek.springstudy.batch.video2018.domain.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

@EnableBatchProcessing
@SpringBootApplication
public class ParallelStepsJobApplication {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parallelStepsJob() {
        Flow secondFlow = new FlowBuilder<Flow>("secondFlow")
                .start(step2())
                .build();

        Flow parallelFlow = new FlowBuilder<Flow>("parallelFlow")
                .start(step1())
                .split(new SimpleAsyncTaskExecutor())
                .add(secondFlow)
                .build();

        return this.jobBuilderFactory.get("parallelStepsJob")
                .start(parallelFlow)
                .end()
                .build();
    }

    // step 순서대로 진행
//    @Bean
//    public Job sequentialStepsJob() {
//        return this.jobBuilderFactory.get("sequentialStepsJob")
//                .start(step1())
//                .start(step2())
//                .build();
//    }

    @Bean
    @StepScope
    public FlatFileItemReader<Transaction> fileTransactionReader(
            @Value("#{jobParameters['inputFlatFile']}") Resource resource
    ) {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionItemReader")
                .resource(new ClassPathResource("batch/bigtransactions.csv"))
                .delimited()
                .names(new String[] {"account", "amount", "timestamp"})
                .fieldSetMapper(fieldSet -> {
                    Transaction transaction = new Transaction();
                    transaction.setAccount(fieldSet.readString("account"));
                    transaction.setAmount(fieldSet.readBigDecimal("amount"));
                    transaction.setTimestamp(fieldSet.readDate("timestamp", "yyyy-MM-dd HH:mm:ss"));

                    return transaction;
                })
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<Transaction> xmlTransactionReader (
            @Value("#{jobParameters['inputXmlFile']}") Resource resource
    ) {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Transaction.class);

        return new StaxEventItemReaderBuilder<Transaction>()
                .name("xmlFileTransactionReader")
                .resource(new ClassPathResource("batch/bigtransactions.xml"))
                .addFragmentRootElements("transaction")
                .unmarshaller(unmarshaller)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Transaction> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(dataSource)
                .sql("INSERT INTO TRANSACTION (ACCOUNT, AMOUNT, TIMESTAMP) VALUES (:account, :amount, :timestamp)")
                .beanMapped()
                .build();
    }

    @Bean
    public Step step1() {

        return this.stepBuilderFactory.get("step1")
                .<Transaction, Transaction> chunk(100)
                .reader(fileTransactionReader(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    public Step step2() {
        return this.stepBuilderFactory.get("step2")
                .<Transaction, Transaction>chunk(100)
                .reader(xmlTransactionReader(null))
                .writer(writer(null))
                .build();
    }

    public static void main(String[] args) {
        String[] newArgs = new String[] {"inputFlatFile=/batch/bigtransactions.csv",
                            "inputXmlFile=/batch/bigtransactions.xml"};

        SpringApplication.run(ParallelStepsJobApplication.class, newArgs);
    }
}
