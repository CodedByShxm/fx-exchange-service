package com.joukou.fxexchangeservice.controller;

import com.joukou.fxexchangeservice.dto.*;
import com.joukou.fxexchangeservice.service.CurrencyConversionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversions")
@RequiredArgsConstructor
public class CurrencyConversionController {

    private final CurrencyConversionService service;

    @PostMapping
    public ConversionResponse convert(
            @Valid @RequestBody ConversionRequest request
    ) {
        return service.convert(request);
    }
}