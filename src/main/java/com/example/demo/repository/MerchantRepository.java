package com.example.demo.repository;

import com.example.demo.model.user.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByReferenceUuid(String uuid);
}
