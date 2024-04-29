package com.backend.restaurant.repository;

import com.backend.restaurant.entity.RestaurantJwtToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantJwtTokenRepository extends JpaRepository<RestaurantJwtToken,Long> {


    @Query(
            nativeQuery = true,
            value = "select * from restaurant_jwt_token "+
                    "where restaurant_id = :restaurantId "+
                    "and (expired = 0 and revoked = 0)"

    )
    List<RestaurantJwtToken> findAllValidTokensByRestaurantId(@Param("restaurantId")Long restaurantId);

    Optional<RestaurantJwtToken> findByJwtToken(String jwtToken);
}
