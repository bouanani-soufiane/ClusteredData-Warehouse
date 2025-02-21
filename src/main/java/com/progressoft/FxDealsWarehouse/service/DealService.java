package com.progressoft.FxDealsWarehouse.service;

import com.progressoft.FxDealsWarehouse.dto.DealRequestDto;
import com.progressoft.FxDealsWarehouse.dto.DealResponseDto;

public interface DealService {
    DealResponseDto save( DealRequestDto dto);

}
