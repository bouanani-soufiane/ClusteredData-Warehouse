package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.dto.DealRequestDto;
import com.progressoft.FxDealsWarehouse.dto.DealResponseDto;
import com.progressoft.FxDealsWarehouse.entity.Deal;
import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import com.progressoft.FxDealsWarehouse.exception.RequestAlreadyExistException;
import com.progressoft.FxDealsWarehouse.mapper.DealMapper;
import com.progressoft.FxDealsWarehouse.repository.DealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(SpringExtension.class)
class DefaultDealServiceTest {
    @Mock
    private CurrencyValidator currencyValidator;

    @Mock
    private DealMapper dealMapper;

    @Mock
    private DealRepository dealRepository;

    @InjectMocks
    private DefaultDealService sut;

    @Test
    void givenInvalidCurrencies_whenSave_thenThrowInvalidCurrencyException () {
        final String invalidCurrency = "usosos";
        final String exceptionMessage = String.format("Invalid deal: fromCurrency [%s] and toCurrency [%s] cannot be the same.", invalidCurrency, invalidCurrency);
        final DealRequestDto request = new DealRequestDto(
                "dealId",
                invalidCurrency,
                invalidCurrency,
                BigDecimal.ONE
        );

        willThrow(new InvalidCurrencyException(exceptionMessage))
                .given(currencyValidator).validateCurrencyExchange(invalidCurrency, invalidCurrency);

        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> sut.save(request))
                .withMessage(exceptionMessage);
    }

    @Test
    void givenExistingDealId_whenSave_thenThrowRequestAlreadyExistsException () {
        final String exceptionMessage = "This request already exists";
        final DealRequestDto request = new DealRequestDto(
                "dealId",
                "USD",
                "MAD",
                BigDecimal.ONE
        );

        given(dealRepository.existsById(request.id()))
                .willReturn(true);

        assertThatExceptionOfType(RequestAlreadyExistException.class)
                .isThrownBy(() -> sut.save(request))
                .withMessage(exceptionMessage);
    }

    @Test
    void givenValidRequest_whenSave_thenSuccess () {
        final DealRequestDto request = new DealRequestDto(
                "dealId",
                "USD",
                "MAD",
                BigDecimal.ONE
        );
        final Deal expected = new Deal(
                request.id(),
                request.fromCurrency(),
                request.toCurrency(),
                null,
                request.dealAmount()
        );

        given(dealRepository.existsById(request.id())).willReturn(false);
        given(dealMapper.toEntity(request)).willReturn(expected);
        given(dealRepository.save(expected)).willReturn(expected);
        given(dealMapper.toResponseEntity(expected)).willReturn(new DealResponseDto(
                expected.getId(),
                expected.getFromCurrency(),
                expected.getToCurrency(),
                expected.getDealTimestamp(),
                expected.getDealAmount()
        ));

        DealResponseDto actual = sut.save(request);

        assertThat(actual).isNotNull();
        assertThat(actual.dealAmount()).isEqualTo(expected.getDealAmount());
        assertThat(actual.fromCurrency()).isEqualTo(expected.getFromCurrency());
        assertThat(actual.toCurrency()).isEqualTo(expected.getToCurrency());
    }

}