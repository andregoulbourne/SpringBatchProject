package com.andre.config;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.andre.entity.Person;

public class PersonProcessor implements ItemProcessor<Person, Person>{

	private static final Logger logger = Logger.getLogger(PersonProcessor.class);
	
	@Override
	public Person process(Person item) throws Exception {
		logger.info(item.getName());
		return item;
	}

	
}
