package ir.jashakouri.data.repo.wallet;

import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.data.entities.Wallet;
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
public interface WalletRepository extends PagingAndSortingRepository<Wallet, UUID> {

    Page<Wallet> findAllByUserAndStatusAndDeleted(User user, Status status, Deleted deleted, Pageable pageable);

    Optional<Wallet> findAllByUserAndCurrencyAndStatusAndDeleted(User user, Currency currency, Status status, Deleted deleted);

    Page<Wallet> findAllByStatusAndDeleted(Status status, Deleted deleted, Pageable pageable);

    @NotNull Optional<Wallet> findById(@NotNull UUID id);
}
