package com.waltwang.maivc.repository;

import com.waltwang.maivc.domain.Userm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.Optional;

public interface UsermRepository extends JpaRepository<Userm, Long> {
    Optional<Userm> findByUsername(String username);
}
