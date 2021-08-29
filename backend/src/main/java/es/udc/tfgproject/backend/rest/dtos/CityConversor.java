package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.City;

public class CityConversor {

	private CityConversor() {
	}

	public final static CityDto toCityDto(City city) {
		return new CityDto(city.getId(), city.getName(), city.getProvince().getName());
	}

	public final static List<CityDto> toCityDtos(List<City> cities) {
		return cities.stream().map(c -> toCityDto(c)).collect(Collectors.toList());
	}

}
