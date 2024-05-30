package tech.buildrun.agregadorinvestimentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.agregadorinvestimentos.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, String> {
}
