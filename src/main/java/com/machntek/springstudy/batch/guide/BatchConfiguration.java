package com.machntek.springstudy.batch.guide;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /**
     * ItemReader를 만든다. sample-data.csv를 읽어서 Person으로 파싱한다.
     */
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("batch/sample-data.csv"))
                .delimited()
                .names(new String[] {"firstName", "lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }

    /**
     *  PersonItemProcessor 의 인스턴스를 만든다.(Upper case로 변환한다는 의미)
     */
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /**
     *  ItemWriter를 만든다. @EnableBatchProcessing에 의해 생성된 데이터 소스의 복사본을 자동으로 가져온다.
     *  Java Bean 속성에 의해 구동되는 단일 Person을 삽입하는데 필요한 SQL문이 포함된다.
     */
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    /**
     *  job을 정의한다. job은 스텝으로 구성되며, 스텝은 각 단계에 reader, processor, writer가 포함될 수 있다.
     *  이 job 정의에서는 job이 실행 상태를 유지하기 위해 DB를 사용하기 때문에 incrementer가 필요하다.
     *  그 후에 각 step을 리스팅 한다.
     *  job이 끝나고 자바 API가 완벽하게 구성된 작업을 생성한다.
     */
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    /**
     *  단일 step을 정의한다.
     *  얼마나 많은 데이터를 한번에 몇개씩 가져올지 정의한다.
     *  이 경우에는, 한번에 10개씩 write한다.
     *  그 후, 앞서 주입된 빈들을 사용하여 reader, processor, writer를 설정한다.
     *  chunk는 제네릭 메소드라서 타입을 지정해줘야한다.
     *  이것은 각 처리 "chunk"의 input과 output 타입을 나타내며, ItemReader<Person> 및 ItemWriter<Person> 과 정렬된다
     */
    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
