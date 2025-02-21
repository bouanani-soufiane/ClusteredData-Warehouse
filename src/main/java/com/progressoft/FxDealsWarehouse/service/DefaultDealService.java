package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.dto.DealRequestDto;
import com.progressoft.FxDealsWarehouse.dto.DealResponseDto;
import com.progressoft.FxDealsWarehouse.entity.Deal;
import com.progressoft.FxDealsWarehouse.exception.CurrencyNotAvailableException;
import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import com.progressoft.FxDealsWarehouse.exception.RequestAlreadyExistException;
import com.progressoft.FxDealsWarehouse.mapper.DealMapper;
import com.progressoft.FxDealsWarehouse.repository.DealRepository;
import com.progressoft.FxDealsWarehouse.util.CurrencyHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultDealService implements DealService {
    private final DealRepository dealRepository;
    private final DealMapper dealMapper;
    private final CurrencyHolder currencyHolder;


    @Override
    public DealResponseDto save ( final DealRequestDto dto ) {
        validateCurrencies(dto);
        log.info("create dealsDTO: {}, {}, {}", dto.fromCurrency(), dto.toCurrency(), dto.dealAmount());
        if (dealRepository.existsById(dto.id())) {
            log.warn("Duplicate deal ID detected: {}. Operation aborted.", dto.id());
            throw new RequestAlreadyExistException("This request already exists");
        }
        Deal deal = dealMapper.toEntity(dto);
        deal.setDealTimestamp(LocalDateTime.now());
        Deal savedDeal = dealRepository.save(deal);
        return dealMapper.toResponseEntity(savedDeal);
    }

    private void validateCurrencies(DealRequestDto dto) {
        if (dto.fromCurrency().equals(dto.toCurrency())) {
            String errorMessage = String.format("Invalid deal: fromCurrency [%s] and toCurrency [%s] cannot be the same.",
                    dto.fromCurrency(), dto.toCurrency());
            log.warn(errorMessage);
            throw new InvalidCurrencyException(errorMessage);
        }

        if (!currencyHolder.exists(dto.fromCurrency())) {
            String errorMessage = String.format("Currency not available: %s", dto.fromCurrency());
            log.warn(errorMessage);
            throw new CurrencyNotAvailableException(errorMessage);
        }

        if (!currencyHolder.exists(dto.toCurrency())) {
            String errorMessage = String.format("Currency not available: %s", dto.toCurrency());
            log.warn(errorMessage);
            throw new CurrencyNotAvailableException(errorMessage);
        }
    }

}
