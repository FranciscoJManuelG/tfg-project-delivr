package es.udc.tfgproject.backend.rest.dtos;

public class AllowDto {

	private Boolean allowed;

	public AllowDto(Boolean allowed) {
		super();
		this.allowed = allowed;
	}

	public AllowDto() {
		super();
	}

	public Boolean getAllowed() {
		return allowed;
	}

	public void setAllowed(Boolean allowed) {
		this.allowed = allowed;
	}

}
