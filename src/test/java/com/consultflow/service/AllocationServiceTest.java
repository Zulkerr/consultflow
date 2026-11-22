package com.consultflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.consultflow.dto.request.CreateAllocationRequest;
import com.consultflow.dto.response.AllocationResponse;
import com.consultflow.entity.Allocation;
import com.consultflow.entity.Consultant;
import com.consultflow.entity.Project;
import com.consultflow.exception.BusinessRuleException;
import com.consultflow.repository.AllocationRepository;
import com.consultflow.repository.ConsultantRepository;
import com.consultflow.repository.ProjectRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AllocationServiceTest {

    @Mock
    private AllocationRepository allocationRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private AllocationService allocationService;

    private Consultant javaConsultant;
    private Project javaProject;

    @BeforeEach
    void setUp() {
        javaConsultant = Consultant.builder()
                .id(1L)
                .name("Anna Weber")
                .email("anna@consultflow.de")
                .primarySkill("Java")
                .maxCapacityPercent(100)
                .build();

        javaProject = Project.builder()
                .id(1L)
                .title("Cloud Migration")
                .clientName("FinBank AG")
                .requiredSkill("Java")
                .status(Project.ProjectStatus.ACTIVE)
                .build();
    }

    @Test
    void allocate_succeeds_whenSkillMatchesAndCapacityAvailable() {
        CreateAllocationRequest req = new CreateAllocationRequest(
                1L, 1L, 60, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31));

        when(consultantRepository.findById(1L)).thenReturn(Optional.of(javaConsultant));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(javaProject));
        when(allocationRepository.sumOverlappingAllocationPercent(1L, req.startDate(), req.endDate())).thenReturn(30);
        when(allocationRepository.save(any(Allocation.class))).thenAnswer(inv -> {
            Allocation saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        AllocationResponse res = allocationService.allocate(req);

        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.allocationPercent()).isEqualTo(60);
        assertThat(res.consultantName()).isEqualTo("Anna Weber");
    }

    @Test
    void allocate_throwsBusinessRuleException_whenCapacityExceeded() {
        CreateAllocationRequest req = new CreateAllocationRequest(
                1L, 1L, 40, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31));

        when(consultantRepository.findById(1L)).thenReturn(Optional.of(javaConsultant));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(javaProject));
        when(allocationRepository.sumOverlappingAllocationPercent(1L, req.startDate(), req.endDate())).thenReturn(70);

        assertThatThrownBy(() -> allocationService.allocate(req))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Capacity exceeded");
    }

    @Test
    void allocate_throwsBusinessRuleException_whenSkillMismatches() {
        javaConsultant.setPrimarySkill("Python");
        CreateAllocationRequest req = new CreateAllocationRequest(
                1L, 1L, 50, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31));

        when(consultantRepository.findById(1L)).thenReturn(Optional.of(javaConsultant));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(javaProject));

        assertThatThrownBy(() -> allocationService.allocate(req))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Skill mismatch");
    }
}