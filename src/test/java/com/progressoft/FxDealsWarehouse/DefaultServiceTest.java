package com.progressoft.FxDealsWarehouse;

import com.progressoft.FxDealsWarehouse.dto.DealRequestDto;
import com.progressoft.FxDealsWarehouse.dto.DealResponseDto;
import com.progressoft.FxDealsWarehouse.entity.Deal;
import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import com.progressoft.FxDealsWarehouse.exception.RequestAlreadyExistException;
import com.progressoft.FxDealsWarehouse.mapper.DealMapper;
import com.progressoft.FxDealsWarehouse.repository.DealRepository;
import com.progressoft.FxDealsWarehouse.service.DefaultDealService;
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

    @InjectMocks
    private DefaultDealService dealService;

    private DealRequestDto dealRequestDto;
    private Deal deal;
    private DealResponseDto dealResponseDto;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp () {
        dealRequestDto = new DealRequestDto(
                "DEAL001",
                "MAD",
                "EUR",
                BigDecimal.valueOf(1000.00)
        );

        deal = new Deal("DEAL001",
                "MAD",
                "EUR",
                now,
                BigDecimal.valueOf(1000.00)
        );

        dealResponseDto = new DealResponseDto("DEAL001",
                "MAD",
                "EUR",
                now,
                BigDecimal.valueOf(1000.00)
        );
    }

    @Nested
    @DisplayName("Save Deal Tests")
    class SaveDealTests {

        @Test
        @DisplayName("Should successfully save a new deal")
        void shouldSaveNewDeal () {
            given(dealRepository.existsById(dealRequestDto.id())).willReturn(false);
            given(dealMapper.toEntity(dealRequestDto)).willReturn(deal);
            given(dealRepository.save(deal)).willReturn(deal);
            given(dealMapper.toResponseEntity(deal)).willReturn(dealResponseDto);

            DealResponseDto result = dealService.save(dealRequestDto);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(dealRequestDto.id());
            assertThat(result.fromCurrency()).isEqualTo(dealRequestDto.fromCurrency());
            assertThat(result.toCurrency()).isEqualTo(dealRequestDto.toCurrency());
            assertThat(result.dealAmount()).isEqualTo(dealRequestDto.dealAmount());

            verify(dealRepository).existsById(dealRequestDto.id());
            verify(dealMapper).toEntity(dealRequestDto);
            verify(dealRepository).save(deal);
            verify(dealMapper).toResponseEntity(deal);
        }

        @Test
        @DisplayName("Should throw DuplicateDealIdException when deal ID already exists")
        void shouldThrowExceptionForDuplicateDealId () {
            given(dealRepository.existsById(dealRequestDto.id())).willReturn(true);

            assertThrows(RequestAlreadyExistException.class,
                    () -> dealService.save(dealRequestDto),
                    "Should throw DuplicateDealIdException");

            verify(dealRepository).existsById(dealRequestDto.id());
            verify(dealRepository, never()).save(any(Deal.class));
        }

        @Test
        @DisplayName("Should throw InvalidCurrencyException when to and from currencies are the same")
        void shouldThrowExceptionWhenCurrenciesAreSame () {
            DealRequestDto invalidDealRequest = new DealRequestDto(
                    "DEAL002",
                    "EUR",
                    "EUR",
                    BigDecimal.valueOf(1000.00));

            InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                    () -> dealService.save(invalidDealRequest),
                    "Should throw InvalidCurrencyException when currencies are the same");

            assertThat(exception.getMessage())
                    .isEqualTo("Invalid deal: fromCurrency [EUR] and toCurrency [EUR] cannot be the same.");

            verify(dealRepository, never()).existsById(any());
            verify(dealRepository, never()).save(any(Deal.class));
        }


    }

}