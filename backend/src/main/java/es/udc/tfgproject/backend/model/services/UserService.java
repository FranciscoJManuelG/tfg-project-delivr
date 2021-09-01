package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectLoginException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectPasswordException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface UserService {

	void signUp(User user) throws DuplicateInstanceException;

	void signUpBusinessman(User user) throws DuplicateInstanceException;

	User login(String userName, String password) throws IncorrectLoginException;

	User loginFromId(Long id) throws InstanceNotFoundException;

	User updateProfile(Long id, String firstName, String lastName, String email, String phone)
			throws InstanceNotFoundException;

	void changePassword(Long id, String oldPassword, String newPassword)
			throws InstanceNotFoundException, IncorrectPasswordException;

	FavouriteAddress addFavouriteAddress(String street, String cp, Long cityId, Long userId)
			throws InstanceNotFoundException;

	void deleteFavouriteAddress(Long userId, Long addressId) throws InstanceNotFoundException, PermissionException;

	Block<FavouriteAddress> findFavouriteAddresses(Long userId, int page, int size);

	Block<FavouriteAddress> findFavouriteAddressesByCity(Long userId, Long cityId, int page, int size);

	FavouriteAddress findFavAddress(Long userId, Long addressId) throws InstanceNotFoundException, PermissionException;

	void payWeeklyBalanceToCompanies(Long userId) throws InstanceNotFoundException, PermissionException;

	User payQuarterlyFee(Long userId) throws InstanceNotFoundException;
}
