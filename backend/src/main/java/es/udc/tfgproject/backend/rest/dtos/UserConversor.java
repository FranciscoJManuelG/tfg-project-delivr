package es.udc.tfgproject.backend.rest.dtos;

import static es.udc.tfgproject.backend.rest.dtos.MenuConversor.toMenuDto;
import static es.udc.tfgproject.backend.rest.dtos.ShoppingCartConversor.toShoppingCartDto;

import es.udc.tfgproject.backend.model.entities.User;

public class UserConversor {

	private UserConversor() {
	}

	public final static UserDto toUserDto(User user) {
		return new UserDto(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getPhone(), user.getRole().toString(), user.getFeePaid());
	}

	public final static User toUser(UserDto userDto) {

		return new User(userDto.getUserName(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(),
				userDto.getEmail(), userDto.getPhone());
	}

	public final static AuthenticatedUserDto toAuthenticatedUserDto(String serviceToken, User user) {

		return new AuthenticatedUserDto(serviceToken, toUserDto(user), toShoppingCartDto(user.getShoppingCart()),
				toMenuDto(user.getMenu()));

	}

}
