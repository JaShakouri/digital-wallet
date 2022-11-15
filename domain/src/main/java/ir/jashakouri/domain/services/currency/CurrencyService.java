package ir.jashakouri.domain.services.currency;

import ir.jashakouri.data.dto.request.CreateCurrencyRequest;
import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.domain.exception.currency.CurrencyExistException;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 29.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
public interface CurrencyService {

    Optional<Currency> findByName(String name) throws CurrencyNotExistException;

    Optional<Currency> find(UUID currencyId) throws CurrencyNotExistException;

    Optional<?> save(CreateCurrencyRequest request) throws CurrencyExistException;

    Optional<?> delete(String id) throws CurrencyNotExistException;

    Optional<?> update(Currency currency, String id) throws CurrencyNotExistException;

    Page<Currency> getAll(int page);

}
