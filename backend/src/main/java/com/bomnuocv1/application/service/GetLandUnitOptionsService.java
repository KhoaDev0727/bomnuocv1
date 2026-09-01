package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.LandUnitOptionResult;
import com.bomnuocv1.application.usecase.GetLandUnitOptionsUseCase;
import com.bomnuocv1.domain.valueobject.LandUnit;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class GetLandUnitOptionsService implements GetLandUnitOptionsUseCase {

    @Override
    public List<LandUnitOptionResult> execute() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        return Arrays.stream(LandUnit.values())
                .map(unit -> LandUnitOptionResult.builder()
                        .code(unit.name())
                        .label(unit.getLabel())
                        .displayName(unit.getDisplayName())
                        .squareMeters(unit.getSquareMeters())
                        .defaultPrice(unit.getDefaultPrice())
                        .formattedDefaultPrice(formatter.format(unit.getDefaultPrice()) + " đ")
                        .description(unit.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
