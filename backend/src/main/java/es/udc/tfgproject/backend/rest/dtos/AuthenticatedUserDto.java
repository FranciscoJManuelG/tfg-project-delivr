package es.udc.tfgproject.backend.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticatedUserDto {

	private String serviceToken;
	private UserDto userDto;
	private ShoppingCartDto shoppingCartDto;
	private MenuDto menuDto;

	public AuthenticatedUserDto() {
	}

	public AuthenticatedUserDto(String serviceToken, UserDto userDto, ShoppingCartDto shoppingCartDto,
			MenuDto menuDto) {

		this.serviceToken = serviceToken;
		this.userDto = userDto;
		this.shoppingCartDto = shoppingCartDto;
		this.menuDto = menuDto;

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

	@JsonProperty("menu")
	public MenuDto getMenuDto() {
		return menuDto;
	}

	public void setMenuDto(MenuDto menuDto) {
		this.menuDto = menuDto;
	}

}
