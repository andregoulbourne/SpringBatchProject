package com.andre.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.andre.entity.Person;
import com.andre.util.ConnectionUtil;
import com.andre.util.Constants;
import com.andre.util.SQLXMLUtility;

@Repository("personDao")
public class PersonDAO {
		
	private static final Logger log = Logger.getLogger(PersonDAO.class);
	
	public List<Person> allPeople() {
		
		List<Person> allPeople = new ArrayList<>();
		
		try (Statement stmt = ConnectionUtil.getConnection().createStatement(); ) {
			
			String sql = SQLXMLUtility.getInstance().getPropertyMap().get("getAllPeople");
			

			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				Person p = new Person();
				p.setId(rs.getInt("PERSON_ID"));
				p.setName(rs.getString("NAME"));
				p.setAge(rs.getString("AGE"));
				allPeople.add(p);	
			}	
		} catch (SQLException e) {
			log.error("We failed to retrieve all profiles!", e);
			return new ArrayList<>();
		}
		
		return allPeople;
	}

	public boolean insertAPerson(Person person) {
		
		boolean result = false;
		String sql = SQLXMLUtility.getInstance().getPropertyMap().get("insertAPersion");
		
		int maxPersonId = getMaxPersionId();
		
		try(PreparedStatement preparedStatement = ConnectionUtil.getConnection().prepareStatement(sql)) { 
			
			preparedStatement.setInt(1, maxPersonId + 1);
			preparedStatement.setString(2, person.getName());
			preparedStatement.setString(3, person.getAge());
			
			return dbSaveIsSuccess(preparedStatement); 
			
		} catch (SQLException e) {
			log.error(Constants.ADDEXCEPTION, e);
		}

		return result;
		
	}
	
	private boolean dbSaveIsSuccess(PreparedStatement preparedStatement) throws SQLException {
		int updateResult = preparedStatement.executeUpdate();
		return (updateResult > 0);
		
	}
	
	private int getMaxPersionId() {
		List<Person> people =allPeople();
		
		int max = 0;
		
		for(Person person: people) {
			if(max < person.getId())
				max = person.getId();
		}
		
		return max;
	}
}
