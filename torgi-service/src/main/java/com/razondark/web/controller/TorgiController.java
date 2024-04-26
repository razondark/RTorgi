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
    public List<LotDto> getLotsBySubject(@RequestParam(value = "subject") Integer subject,
                                         @RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "size", required = false) Integer size) {
        return torgiService.getLothBySubject(subject, page, size);
    }

    @GetMapping("bidd-type")
    public List<LotDto> getLotsByBiddType(@RequestParam(value = "subject") Integer subject,
                                         @RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "size", required = false) Integer size) {
        return torgiService.getLothBySubject(subject, page, size);
    }
}
