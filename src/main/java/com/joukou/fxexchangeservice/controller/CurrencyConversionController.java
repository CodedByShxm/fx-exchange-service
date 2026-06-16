package com.joukou.fxexchangeservice.controller;

import com.joukou.fxexchangeservice.dto.*;
import com.joukou.fxexchangeservice.service.CurrencyConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FX Conversion API")
@RestController
@RequestMapping("/api/v1/conversions")
@RequiredArgsConstructor
public class CurrencyConversionController {

    private final CurrencyConversionService service;

    @Operation(summary = "Convert currency")
    @PostMapping
    public ConversionResponse convert(
            @Valid @RequestBody ConversionRequest request
    ) {
        return service.convert(request);
    }
}