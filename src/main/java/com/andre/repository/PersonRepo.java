package com.andre.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andre.entity.Person;

public interface PersonRepo extends JpaRepository<Person, Integer> {

}
