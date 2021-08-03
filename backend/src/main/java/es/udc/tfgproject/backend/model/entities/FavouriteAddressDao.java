package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FavouriteAddressDao extends PagingAndSortingRepository<FavouriteAddress, Long> {

	Optional<FavouriteAddress> findByAddressId(Long addressId);

	Slice<FavouriteAddress> findByUserId(Long userId, Pageable pageable);

	Slice<FavouriteAddress> findByUserIdAndAddressCityId(Long userId, Long cityId, Pageable pageable);
}
