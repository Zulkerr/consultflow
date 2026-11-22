package com.consultflow.service;

import com.consultflow.dto.request.CreateConsultantRequest;
import com.consultflow.dto.response.ConsultantResponse;
import com.consultflow.entity.Consultant;
import com.consultflow.exception.BusinessRuleException;
import com.consultflow.exception.ResourceNotFoundException;
import com.consultflow.repository.ConsultantRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultantService {

    private final ConsultantRepository consultantRepository;

    @Transactional
    public ConsultantResponse create(CreateConsultantRequest request) {
        if (consultantRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Consultant with email '" + request.email() + "' already exists");
        }
        int capacity = request.maxCapacityPercent() != null ? request.maxCapacityPercent() : 100;
        Consultant consultant = Consultant.builder()
                .name(request.name())
                .email(request.email())
                .primarySkill(request.primarySkill())
                .maxCapacityPercent(capacity)
                .build();
        return ConsultantResponse.from(consultantRepository.save(consultant));
    }

    public List<ConsultantResponse> findAll(String skill) {
        List<Consultant> consultants = (skill != null && !skill.isBlank())
                ? consultantRepository.findByPrimarySkillIgnoreCase(skill.trim())
                : consultantRepository.findAll();
        return consultants.stream().map(ConsultantResponse::from).toList();
    }

    public ConsultantResponse findById(Long id) {
        return consultantRepository.findById(id)
                .map(ConsultantResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Consultant not found with id: " + id));
    }
}