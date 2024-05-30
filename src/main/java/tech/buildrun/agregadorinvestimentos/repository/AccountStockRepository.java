package tech.buildrun.agregadorinvestimentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.agregadorinvestimentos.entity.AccountStock;
import tech.buildrun.agregadorinvestimentos.entity.id.AccountStockId;

public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
