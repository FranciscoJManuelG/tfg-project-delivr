package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageDao extends PagingAndSortingRepository<Image, Long> {

	Optional<Image> findByPath(String path);

}
