package com.spring.redditclone.application.repository;

import com.spring.redditclone.application.model.VerficationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository   extends JpaRepository<VerficationToken, Long> {

//    @Query("Select v from VerficationToken v where v.token=token")
    Optional<VerficationToken> findByToken(String token);
}
