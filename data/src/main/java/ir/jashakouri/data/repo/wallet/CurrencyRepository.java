package ir.jashakouri.data.repo.wallet;

import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.data.enums.Deleted;
import ir.jashakouri.data.enums.Status;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 22.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Repository
public interface CurrencyRepository extends PagingAndSortingRepository<Currency, UUID> {
    Optional<Currency> findByName(String name);

    @NotNull Optional<Currency> findById(@NotNull UUID id);

    Page<Currency> findAllByStatusAndDeleted(Pageable pageable, Status status, Deleted deleted);

}
