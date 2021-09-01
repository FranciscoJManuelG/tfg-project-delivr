package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.FavouriteAddressConversor.toFavouriteAddressDto;
import static es.udc.tfgproject.backend.rest.dtos.FavouriteAddressConversor.toFavouriteAddressSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.UserConversor.toAuthenticatedUserDto;
import static es.udc.tfgproject.backend.rest.dtos.UserConversor.toUser;
import static es.udc.tfgproject.backend.rest.dtos.UserConversor.toUserDto;

import java.net.URI;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectLoginException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectPasswordException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.Constantes;
import es.udc.tfgproject.backend.model.services.UserService;
import es.udc.tfgproject.backend.rest.common.ErrorsDto;
import es.udc.tfgproject.backend.rest.common.JwtGenerator;
import es.udc.tfgproject.backend.rest.common.JwtInfo;
import es.udc.tfgproject.backend.rest.dtos.AddFavouriteAddressParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AuthenticatedUserDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.ChangePasswordParamsDto;
import es.udc.tfgproject.backend.rest.dtos.FavouriteAddressDto;
import es.udc.tfgproject.backend.rest.dtos.FavouriteAddressSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.LoginParamsDto;
import es.udc.tfgproject.backend.rest.dtos.UserDto;

@RestController
@RequestMapping("/users")
public class UserController {

	private final static String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";
	private final static String INCORRECT_PASSWORD_EXCEPTION_CODE = "project.exceptions.IncorrectPasswordException";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JwtGenerator jwtGenerator;

	@Autowired
	private UserService userService;

	@ExceptionHandler(IncorrectLoginException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
				INCORRECT_LOGIN_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(IncorrectPasswordException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectPasswordException(IncorrectPasswordException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(INCORRECT_PASSWORD_EXCEPTION_CODE, null,
				INCORRECT_PASSWORD_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@PostMapping("/signUp")
	public ResponseEntity<AuthenticatedUserDto> signUp(@Validated @RequestBody UserDto userDto)
			throws DuplicateInstanceException {

		User user = toUser(userDto);

		userService.signUp(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
				.toUri();

		return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));

	}

	@PostMapping("/signUpBusinessman")
	public ResponseEntity<AuthenticatedUserDto> signUpBusinessman(@Validated @RequestBody UserDto userDto)
			throws DuplicateInstanceException {

		User user = toUser(userDto);

		userService.signUpBusinessman(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
				.toUri();

		return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));

	}

	@PostMapping("/login")
	public AuthenticatedUserDto login(@Validated @RequestBody LoginParamsDto params) throws IncorrectLoginException {

		User user = userService.login(params.getUserName(), params.getPassword());

		return toAuthenticatedUserDto(generateServiceToken(user), user);

	}

	@PostMapping("/loginFromServiceToken")
	public AuthenticatedUserDto loginFromServiceToken(@RequestAttribute Long userId,
			@RequestAttribute String serviceToken) throws InstanceNotFoundException {

		User user = userService.loginFromId(userId);

		return toAuthenticatedUserDto(serviceToken, user);

	}

	@PutMapping("/{id}")
	public UserDto updateProfile(@RequestAttribute Long userId, @PathVariable Long id,
			@Validated @RequestBody UserDto userDto) throws InstanceNotFoundException, PermissionException {

		if (!id.equals(userId)) {
			throw new PermissionException();
		}

		return toUserDto(userService.updateProfile(id, userDto.getFirstName(), userDto.getLastName(),
				userDto.getEmail(), userDto.getPhone()));

	}

	@PostMapping("/{id}/changePassword")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@RequestAttribute Long userId, @PathVariable Long id,
			@Validated @RequestBody ChangePasswordParamsDto params)
			throws PermissionException, InstanceNotFoundException, IncorrectPasswordException {

		if (!id.equals(userId)) {
			throw new PermissionException();
		}

		userService.changePassword(id, params.getOldPassword(), params.getNewPassword());

	}

	@PostMapping("/favouriteAddresses")
	public FavouriteAddressDto addFavouriteAddress(@RequestAttribute Long userId,
			@Validated @RequestBody AddFavouriteAddressParamsDto params) throws InstanceNotFoundException {

		return toFavouriteAddressDto(
				userService.addFavouriteAddress(params.getStreet(), params.getCp(), params.getCityId(), userId));

	}

	@DeleteMapping("/favouriteAddresses/{addressId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCompanyAddress(@RequestAttribute Long userId, @PathVariable Long addressId)
			throws InstanceNotFoundException, PermissionException {

		userService.deleteFavouriteAddress(userId, addressId);

	}

	@GetMapping("/favouriteAddresses")
	public BlockDto<FavouriteAddressSummaryDto> findFavouriteAddresses(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") int page) {

		Block<FavouriteAddress> addressBlock = userService.findFavouriteAddresses(userId, page, 10);

		return new BlockDto<>(toFavouriteAddressSummaryDtos(addressBlock.getItems()), addressBlock.getExistMoreItems());

	}

	@GetMapping("/favouriteAddress/{addressId}")
	public FavouriteAddressDto findFavAddress(@RequestAttribute Long userId, @PathVariable Long addressId)
			throws InstanceNotFoundException, PermissionException {

		return toFavouriteAddressDto(userService.findFavAddress(userId, addressId));
	}

	private String generateServiceToken(User user) {

		JwtInfo jwtInfo = new JwtInfo(user.getId(), user.getUserName(), user.getRole().toString());

		return jwtGenerator.generate(jwtInfo);

	}

	@GetMapping("/favouriteAddressesByCity")
	public BlockDto<FavouriteAddressSummaryDto> findFavouriteAddressesByCity(@RequestAttribute Long userId,
			@RequestParam Long cityId, @RequestParam(defaultValue = "0") int page) {

		Block<FavouriteAddress> addressBlock = userService.findFavouriteAddressesByCity(userId, cityId, page,
				Constantes.SIZE);

		return new BlockDto<>(toFavouriteAddressSummaryDtos(addressBlock.getItems()), addressBlock.getExistMoreItems());

	}

	@PostMapping("/payFee")
	public UserDto payQuarterlyFee(@RequestAttribute Long userId) throws InstanceNotFoundException {

		return toUserDto(userService.payQuarterlyFee(userId));
	}

	@PostMapping("/payWeeklyBalance")
	public void payWeeklyBalance(@RequestAttribute Long userId) throws InstanceNotFoundException, PermissionException {

		userService.payWeeklyBalanceToCompanies(userId);	}

}
