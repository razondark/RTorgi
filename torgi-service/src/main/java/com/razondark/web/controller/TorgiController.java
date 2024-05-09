package com.razondark.web.controller;

import com.razondark.service.TorgiService;
import com.razondark.web.dto.LotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/torgi/lots/search")
@RequiredArgsConstructor
public class TorgiController {
    private final TorgiService torgiService;

    @GetMapping("all")
    public List<LotDto> getLotsAll(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "size", required = false) Integer size) {
        return torgiService.getLotsAll(page, size);
    }

    @GetMapping("subject")
    public List<LotDto> getLotsBySubject(@RequestParam(value = "subject") String subjects,
                                         @RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "size", required = false) Integer size) {
        return torgiService.getLotsBySubject(subjects, page, size);
    }

    @GetMapping
    public List<LotDto> getLots(@RequestParam(value = "end-date-from", required = false) String endDateFrom,
                                @RequestParam(value = "end-date-to", required = false) String endDateTo,
                                @RequestParam(value = "regions", required = false) String regions,
                                @RequestParam(value = "category", required = false) String category,
                                @RequestParam(value = "permitted-use", required = false) String permittedUse,
                                @RequestParam(value = "square-from", required = false) String squareFrom,
                                @RequestParam(value = "square-to", required = false) String squareTo,
                                @RequestParam(value = "start-price-from", required = false) String startPriceFrom,
                                @RequestParam(value = "start-price-to", required = false) String startPriceTo,
                                @RequestParam(value = "cad-cost-from", required = false) String cadCostFrom,
                                @RequestParam(value = "cad-cost-to", required = false) String cadCostTo,
                                @RequestParam(value = "percent-price-cad-from", required = false) String percentPriceCadFrom,
                                @RequestParam(value = "percent-price-cad-to", required = false) String percentPriceCadTo,
                                @RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "size", required = false) Integer size,
                                @RequestParam(value = "text", required = false) String text) {
        return torgiService.getLots(endDateFrom, endDateTo, regions, category, permittedUse, squareFrom, squareTo,
                startPriceFrom, startPriceTo, cadCostFrom, cadCostTo, percentPriceCadFrom, percentPriceCadTo,
                page, size, text);
    }

    // TODO: write method
//    @GetMapping("bidd-type")
//    public List<LotDto> getLotsByBiddType(@RequestParam(value = "subject") Integer subject,
//                                         @RequestParam(value = "page", required = false) Integer page,
//                                         @RequestParam(value = "size", required = false) Integer size) {
//        torgiService.пуе(subject, page, size);
//    }
}
