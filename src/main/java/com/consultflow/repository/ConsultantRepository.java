package com.consultflow.repository;

import com.consultflow.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

    boolean existsByEmail(String email);

    List<Consultant> findByPrimarySkillIgnoreCase(String primarySkill);
}