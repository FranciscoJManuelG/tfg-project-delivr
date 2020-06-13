package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.CompanyCategory;

public class CompanyCategoryConversor {

	private CompanyCategoryConversor() {
	}

	public final static CompanyCategoryDto toCompanyCategoryDto(CompanyCategory category) {
		return new CompanyCategoryDto(category.getId(), category.getName());
	}

	public final static List<CompanyCategoryDto> toCompanyCategoryDtos(List<CompanyCategory> categories) {
		return categories.stream().map(c -> toCompanyCategoryDto(c)).collect(Collectors.toList());
	}

}
