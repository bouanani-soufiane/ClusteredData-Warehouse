package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.dto.DealRequestDto;
import com.progressoft.FxDealsWarehouse.dto.DealResponseDto;
import com.progressoft.FxDealsWarehouse.entity.Deal;
import com.progressoft.FxDealsWarehouse.exception.RequestAlreadyExistException;
import com.progressoft.FxDealsWarehouse.mapper.DealMapper;
import com.progressoft.FxDealsWarehouse.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultDealService implements DealService {
    private final DealRepository dealRepository;
    private final DealMapper dealMapper;
    private final CurrencyValidator currencyValidator;


    @Override
    public DealResponseDto save ( final DealRequestDto dto ) {

        currencyValidator.validateCurrencyExchange(dto.fromCurrency(), dto.toCurrency());

        log.info("create dealsDTO: {}, {}, {}", dto.fromCurrency(), dto.toCurrency(), dto.dealAmount());

        if (dealRepository.existsById(dto.id())) {
            log.warn("Duplicate deal ID detected: {}. Operation aborted.", dto.id());
            throw new RequestAlreadyExistException("This request already exists");
        }

        final Deal deal = dealMapper.toEntity(dto);
        final Deal savedDeal = dealRepository.save(deal);

        return dealMapper.toResponseEntity(savedDeal);
    }

}
