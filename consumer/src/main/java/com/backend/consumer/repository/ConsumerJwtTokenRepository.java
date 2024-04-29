package com.backend.consumer.repository;

import com.backend.consumer.entity.ConsumerJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumerJwtTokenRepository extends JpaRepository<ConsumerJwtToken, Long> {

    @Query(
            nativeQuery = true,
            value = "select * from consumer_jwt_token " +
                    "where consumer_id = :consumerId " +
                    "and (expired = 0 and revoked = 0)"

    )
    List<ConsumerJwtToken> findAllValidTokensByConsumerId(@Param("consumerId")Long consumerId);

    Optional<ConsumerJwtToken> findByJwtToken(String jwtToken);
}
