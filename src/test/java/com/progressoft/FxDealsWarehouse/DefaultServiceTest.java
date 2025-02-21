package com.progressoft.FxDealsWarehouse;

import com.progressoft.FxDealsWarehouse.dto.DealRequestDto;
import com.progressoft.FxDealsWarehouse.dto.DealResponseDto;
import com.progressoft.FxDealsWarehouse.entity.Deal;
import com.progressoft.FxDealsWarehouse.exception.CurrencyNotAvailableException;
import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import com.progressoft.FxDealsWarehouse.exception.RequestAlreadyExistException;
import com.progressoft.FxDealsWarehouse.mapper.DealMapper;
import com.progressoft.FxDealsWarehouse.repository.DealRepository;
import com.progressoft.FxDealsWarehouse.service.DefaultDealService;
import com.progressoft.FxDealsWarehouse.util.CurrencyHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultServiceTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealMapper dealMapper;

    @Mock
    private CurrencyHolder currencyHolder;

    @InjectMocks
    private DefaultDealService dealService;

    private DealRequestDto dealRequestDto;
    private Deal deal;
    private DealResponseDto dealResponseDto;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        dealRequestDto = new DealRequestDto(
                "DEAL001",
                "USD",
                "EUR",
                BigDecimal.valueOf(1000.00)
        );

        deal = new Deal("DEAL001",
                "USD",
                "EUR",
                now,
                BigDecimal.valueOf(1000.00)
        );

        dealResponseDto = new DealResponseDto("DEAL001",
                "USD",
                "EUR",
                now,
                BigDecimal.valueOf(1000.00)
        );
    }

    @Nested
    @DisplayName("Deal Validation and Save Tests")
    class DealValidationAndSaveTests {

        @Test
        @DisplayName("Should successfully save when all validations pass")
        void shouldSaveWhenAllValidationsPass() {

            given(currencyHolder.exists("USD")).willReturn(true);
            given(currencyHolder.exists("EUR")).willReturn(true);
            given(dealRepository.existsById(dealRequestDto.id())).willReturn(false);
            given(dealMapper.toEntity(dealRequestDto)).willReturn(deal);
            given(dealRepository.save(deal)).willReturn(deal);
            given(dealMapper.toResponseEntity(deal)).willReturn(dealResponseDto);

            DealResponseDto result = dealService.save(dealRequestDto);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo("DEAL001");
            assertThat(result.fromCurrency()).isEqualTo("USD");
            assertThat(result.toCurrency()).isEqualTo("EUR");
            assertThat(result.dealAmount()).isEqualTo(BigDecimal.valueOf(1000.00));

            verify(currencyHolder).exists("USD");
            verify(currencyHolder).exists("EUR");
            verify(dealRepository).existsById("DEAL001");
            verify(dealMapper).toEntity(dealRequestDto);
            verify(dealRepository).save(deal);
            verify(dealMapper).toResponseEntity(deal);
        }

        @Test
        @DisplayName("Should throw RequestAlreadyExistException for duplicate deal ID")
        void shouldThrowExceptionForDuplicateDealId() {

            given(currencyHolder.exists("USD")).willReturn(true);
            given(currencyHolder.exists("EUR")).willReturn(true);
            given(dealRepository.existsById("DEAL001")).willReturn(true);


            RequestAlreadyExistException exception = assertThrows(RequestAlreadyExistException.class,
                    () -> dealService.save(dealRequestDto));

            assertThat(exception.getMessage()).isEqualTo("This request already exists");
            verify(dealRepository).existsById("DEAL001");
            verify(dealRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw InvalidCurrencyException for same currencies")
        void shouldThrowExceptionForSameCurrencies() {

            DealRequestDto sameCurrencyDeal = new DealRequestDto(
                    "DEAL002",
                    "USD",
                    "USD",
                    BigDecimal.valueOf(1000.00)
            );


            InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                    () -> dealService.save(sameCurrencyDeal));

            assertThat(exception.getMessage())
                    .isEqualTo("Invalid deal: fromCurrency [USD] and toCurrency [USD] cannot be the same.");
            verify(currencyHolder, never()).exists(any());
            verify(dealRepository, never()).existsById(any());
            verify(dealRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw CurrencyNotAvailableException for invalid fromCurrency")
        void shouldThrowExceptionForInvalidFromCurrency() {

            DealRequestDto invalidFromCurrencyDeal = new DealRequestDto(
                    "DEAL003",
                    "XXX",
                    "EUR",
                    BigDecimal.valueOf(1000.00)
            );
            given(currencyHolder.exists("XXX")).willReturn(false);


            CurrencyNotAvailableException exception = assertThrows(CurrencyNotAvailableException.class,
                    () -> dealService.save(invalidFromCurrencyDeal));

            assertThat(exception.getMessage()).isEqualTo("Currency not available: XXX");
            verify(currencyHolder).exists("XXX");
            verify(currencyHolder, never()).exists("EUR");
            verify(dealRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw CurrencyNotAvailableException for invalid toCurrency")
        void shouldThrowExceptionForInvalidToCurrency() {

            DealRequestDto invalidToCurrencyDeal = new DealRequestDto(
                    "DEAL004",
                    "USD",
                    "XXX",
                    BigDecimal.valueOf(1000.00)
            );
            given(currencyHolder.exists("USD")).willReturn(true);
            given(currencyHolder.exists("XXX")).willReturn(false);


            CurrencyNotAvailableException exception = assertThrows(CurrencyNotAvailableException.class,
                    () -> dealService.save(invalidToCurrencyDeal));

            assertThat(exception.getMessage()).isEqualTo("Currency not available: XXX");
            verify(currencyHolder).exists("USD");
            verify(currencyHolder).exists("XXX");
            verify(dealRepository, never()).save(any());
        }
    }
}