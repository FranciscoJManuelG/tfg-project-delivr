package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CityDao extends PagingAndSortingRepository<City, Long> {

	List<City> findByProvinceId(Long provinceId);

}