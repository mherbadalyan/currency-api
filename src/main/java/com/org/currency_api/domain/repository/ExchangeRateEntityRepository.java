package com.org.currency_api.domain.repository;

import com.org.currency_api.domain.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRateEntityRepository extends JpaRepository<ExchangeRateEntity,Long> {

    @Query("SELECT currency FROM ExchangeRateEntity")
    List<String> findCurrencies();

    @Query("SELECT entity FROM ExchangeRateEntity entity WHERE entity.currency= :currency")
    ExchangeRateEntity findByCurrency(@Param("currency") String currency);
}
