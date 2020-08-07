package es.udc.tfgproject.backend.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticatedUserDto {

	private String serviceToken;
	private UserDto userDto;
	private ShoppingCartDto shoppingCartDto;

	public AuthenticatedUserDto() {
	}

	public AuthenticatedUserDto(String serviceToken, UserDto userDto, ShoppingCartDto shoppingCartDto) {

		this.serviceToken = serviceToken;
		this.userDto = userDto;
		this.shoppingCartDto = shoppingCartDto;

	}

	public String getServiceToken() {
		return serviceToken;
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
	}

	@JsonProperty("user")
	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	@JsonProperty("shoppingCart")
	public ShoppingCartDto getShoppingCartDto() {
		return shoppingCartDto;
	}

	public void setShoppingCartDto(ShoppingCartDto shoppingCartDto) {
		this.shoppingCartDto = shoppingCartDto;
	}


}
