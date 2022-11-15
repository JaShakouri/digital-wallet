package ir.jashakouri.domain.services.currency;

import ir.jashakouri.domain.exception.currency.CurrencyExistException;
import ir.jashakouri.domain.exception.currency.CurrencyNotExistException;
import ir.jashakouri.data.dto.request.CreateCurrencyRequest;
import ir.jashakouri.data.entities.Currency;
import ir.jashakouri.data.enums.Deleted;
import ir.jashakouri.data.enums.Status;
import ir.jashakouri.data.repo.wallet.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jashakouri on 29.08.22
 * @project Digital Wallet
 * @email JaShakouri@gmail.com
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "LOG_CurrencyServiceImpl")
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ModelMapper modelMapper;

    @Value("${spring.data.web.pageable.default-page-size}")
    public int PageSize;

    @Override
    public Optional<Currency> findByName(String name) {
        return currencyRepository.findByName(name);
    }

    @Override
    public Optional<Currency> find(UUID currencyId)
            throws CurrencyNotExistException {
        var wallet = currencyRepository.findById(currencyId);
        if (wallet.isEmpty()) throw new CurrencyNotExistException();
        return wallet;
    }

    @Override
    public Optional<?> save(CreateCurrencyRequest request) throws CurrencyExistException {
        Optional<Currency> currencyExists = currencyRepository.findByName(request.getName());
        if (currencyExists.isPresent())
            throw new CurrencyExistException();

        return Optional.of(currencyRepository.save(modelMapper.map(request, Currency.class)));
    }

    @Override
    public Optional<?> delete(String id) throws CurrencyNotExistException {
        Optional<Currency> currencyExists = currencyRepository.findById(UUID.fromString(id));

        if (currencyExists.isEmpty()) throw new CurrencyNotExistException(id);

        currencyExists.ifPresent(currency -> currency.setDeleted(Deleted.Active));

        return currencyExists;
    }

    @Override
    public Optional<?> update(Currency currency, String id) throws CurrencyNotExistException {
        Optional<Currency> currencyExists = currencyRepository.findById(UUID.fromString(id));
        if (currencyExists.isEmpty()) throw new CurrencyNotExistException();

        if (!currencyExists.get().getName().equals(currency.getName()))
            currencyExists.get().setName(currency.getName());

        if (!currencyExists.get().getUsdEquivalent().equals(currency.getUsdEquivalent()))
            currencyExists.get().setUsdEquivalent(currency.getUsdEquivalent());

        return currencyExists;
    }

    @Override
    public Page<Currency> getAll(int page) {
        return currencyRepository
                .findAllByStatusAndDeleted(
                        PageRequest.of(page - 1, PageSize), Status.Active, Deleted.InActive);
    }
}
