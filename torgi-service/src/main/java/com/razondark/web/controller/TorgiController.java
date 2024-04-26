package com.razondark.web.controller;

import com.razondark.service.TorgiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/torgi/lots/search")
@RequiredArgsConstructor
public class TorgiController {
    private final TorgiService torgiService;

    @GetMapping
    public String getLots(@RequestParam("page") int page, @RequestParam("size") int size) {
        return torgiService.getLots(page, size);
    }
}
