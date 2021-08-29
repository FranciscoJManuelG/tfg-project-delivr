package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Province;

public class ProvinceConversor {

	private ProvinceConversor() {
	}

	public final static ProvinceDto toProvinceDto(Province province) {
		return new ProvinceDto(province.getId(), province.getName());
	}

	public final static List<ProvinceDto> toProvinceDtos(List<Province> provinces) {
		return provinces.stream().map(c -> toProvinceDto(c)).collect(Collectors.toList());
	}

}
