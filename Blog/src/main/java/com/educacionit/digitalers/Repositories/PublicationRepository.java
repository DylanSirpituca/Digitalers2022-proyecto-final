package com.educacionit.digitalers.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.educacionit.digitalers.entities.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
	List<Publication> findByUserId(Long id);
}