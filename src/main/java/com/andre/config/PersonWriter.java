package com.andre.config;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.andre.entity.Person;
import com.andre.repository.PersonRepo;

public class PersonWriter implements ItemWriter<Person> {

	@Autowired
	private PersonRepo personRepo;
	
	@Override
	public void write(Chunk<? extends Person> chunk) throws Exception {
		System.out.println("Hello");
		System.out.println(chunk);
		chunk.getItems().forEach(obj -> System.out.println(obj.getName()));
		Iterable<Person> people = (Iterable<Person>) chunk.getItems();
		personRepo.saveAll(people);
	}

}
