package ir.jashakouri.data.repo.auth;

import ir.jashakouri.data.entities.User;
import org.jetbrains.annotations.NotNull;
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
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

    @NotNull Optional<User> findById(@NotNull UUID uuid);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
