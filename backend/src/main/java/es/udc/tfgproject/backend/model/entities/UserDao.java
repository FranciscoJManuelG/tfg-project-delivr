package es.udc.tfgproject.backend.model.entities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import es.udc.tfgproject.backend.model.entities.User.RoleType;

public interface UserDao extends PagingAndSortingRepository<User, Long> {

	boolean existsByUserName(String userName);

	Optional<User> findByUserName(String userName);

	List<User> findByRole(RoleType role);

}
