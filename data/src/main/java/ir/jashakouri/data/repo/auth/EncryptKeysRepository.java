package ir.jashakouri.data.repo.auth;

import ir.jashakouri.data.entities.EncryptKeys;
import ir.jashakouri.data.entities.User;
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
public interface EncryptKeysRepository extends PagingAndSortingRepository<EncryptKeys, UUID> {

    Optional<EncryptKeys> findByUser(User user);

}
