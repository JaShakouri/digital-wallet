package ir.jashakouri.data.repo.transaction;

import ir.jashakouri.data.entities.Transaction;
import ir.jashakouri.data.entities.User;
import ir.jashakouri.data.enums.Deleted;
import ir.jashakouri.data.enums.Status;
import ir.jashakouri.data.enums.TransactionStatus;
import ir.jashakouri.data.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, UUID> {

    Page<Transaction> findAllByType(Pageable pageable, TransactionType transactionType);

    Page<Transaction> findAllByUserAndType(Pageable pageable, User user, TransactionType transactionType);

    Optional<Transaction> findByToken(String token);
}
