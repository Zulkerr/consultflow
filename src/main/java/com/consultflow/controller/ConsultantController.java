package com.consultflow.controller;

import com.consultflow.dto.request.CreateConsultantRequest;
import com.consultflow.dto.response.ConsultantResponse;
import com.consultflow.service.ConsultantService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/consultants")
@RequiredArgsConstructor
public class ConsultantController {

    private final ConsultantService consultantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultantResponse create(@Valid @RequestBody CreateConsultantRequest request) {
        return consultantService.create(request);
    }

    @GetMapping
    public List<ConsultantResponse> findAll(@RequestParam(required = false) String skill) {
        return consultantService.findAll(skill);
    }

    @GetMapping("/{id}")
    public ConsultantResponse findById(@PathVariable Long id) {
        return consultantService.findById(id);
    }
}