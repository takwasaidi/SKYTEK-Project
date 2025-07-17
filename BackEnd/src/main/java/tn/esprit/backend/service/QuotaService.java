package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.backend.repository.QuotaRepository;

@Service
@RequiredArgsConstructor
public class QuotaService {
    private final QuotaRepository quotaRepository;
}
