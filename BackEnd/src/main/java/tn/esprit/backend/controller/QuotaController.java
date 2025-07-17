package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.backend.service.QuotaService;

@RestController
@RequestMapping("/api/quota")
@RequiredArgsConstructor
public class QuotaController {
    private final QuotaService quotaService;

}
