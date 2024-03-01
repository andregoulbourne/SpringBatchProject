package com.andre.config;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.andre.entity.Person;
import com.andre.repository.PersonRepo;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SpringBatchConfig {
	
	private static final Logger logger = Logger.getLogger(SpringBatchConfig.class);
	
	// TODO: would work with JDBC, has issues with JPA, and I don't think the scalar app follow this archetechure (the beans are most likely separated out and probably just extends a class)
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private PersonRepo personRepo;
	
	@Autowired
   // private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	private JpaTransactionManager transactionManager;
	//private PlatformTransactionManager transactionManager;
	
	@Bean
	public FlatFileItemReader<Person> reader(){
		FlatFileItemReader<Person> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource(new File("./src/main/resources/spring-batch-practive.csv")));
		itemReader.setName("csvReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}
	
	private LineMapper<Person> lineMapper() {
		DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id","name", "age");
		
		BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Person.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	// Things in program always occur in order, check the input and output to see if the something is blocking execution, if not as expected happening.
	@Bean
	public PersonProcessor processor() {
		return new PersonProcessor();
	}

/*	
	@Bean // Working
	public PersonWriter writer(){
		return new PersonWriter();
	}
*/	
	
		
	@Bean // Working
	public RepositoryItemWriter<Person> writer(){
		RepositoryItemWriter<Person> writer = new RepositoryItemWriter<>();
		writer.setRepository(personRepo);
		writer.setMethodName("save");
		return writer;
	}
	
	/*
	@Bean // Working
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource){
		JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();

	    itemWriter.setDataSource(dataSource);
	    String sql = SQLXMLUtility.getInstance().getPropertyMap().get("insertAPersion");
	    logger.info(sql);
	    itemWriter.setSql(sql);
	    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
	    itemWriter.afterPropertiesSet();

	    return itemWriter;
	}
	*/

	@Bean
	public Step step1() {
		   return new StepBuilder("csv-step", jobRepository)
				   // There are different transaction managers depending on y
			        .<Person, Person>chunk(10, transactionManager)
			        .reader(reader())
			        .processor(processor())
			        .writer(writer(/*ConnectionUtil.getDataSource()*/))
			        .build();
	}
	
	@Bean 
	public Job job() {
		return new JobBuilder("jobName", jobRepository)
				.start(step1())
				.build();
	}
	
}
