package com.apoorva.UserAssignment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apoorva.UserAssignment.domain.CustomerEntity;

import jakarta.transaction.Transactional;

/*
 * A Customer Repository to perform queries with Database 
 * Implementation is provided runTime by JPA Repository
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	@Query("SELECT c FROM CustomerEntity c WHERE c.fileName = :filename")
	List<CustomerEntity> findByFilename(String filename);

	@Modifying
	@Transactional
	@Query("DELETE FROM CustomerEntity c WHERE c.fileName = :filename")
	void deleteByFilename(String filename);

	Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);
}
