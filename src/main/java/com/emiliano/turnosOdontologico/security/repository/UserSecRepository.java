package com.emiliano.turnosOdontologico.security.repository;

import com.emiliano.turnosOdontologico.security.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecRepository extends JpaRepository<UserSec,Long> {
    //Crea la sentencia en base al nombre en inglés del método//

    Optional<UserSec> findUserEntityByUsername(String username);
}
