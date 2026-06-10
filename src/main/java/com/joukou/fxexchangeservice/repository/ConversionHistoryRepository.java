package com.joukou.fxexchangeservice.repository;

import com.joukou.fxexchangeservice.entity.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long>
{
}
