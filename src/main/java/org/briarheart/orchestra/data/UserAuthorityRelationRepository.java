package org.briarheart.orchestra.data;

import org.briarheart.orchestra.model.UserAuthorityRelation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * @author Roman Chigvintsev
 */
public interface UserAuthorityRelationRepository extends ReactiveCrudRepository<UserAuthorityRelation, Void> {
    @Query("SELECT * FROM authorities WHERE email = :email")
    Flux<UserAuthorityRelation> findByEmail(String email);
}
