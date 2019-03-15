package com.membership.controller;

import com.membership.exception.BadRequestException;
import com.membership.model.Plan;
import com.membership.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/plan")
public class PlanController {

    @Autowired
    private PlanRepository planRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plan createPlan(@RequestBody Plan plan) throws BadRequestException {
        checkPlan(plan);

        planRepository.save(plan);
        return plan;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Plan getPlanById(@PathVariable Long id) throws BadRequestException {
        if(id == null)
            throw new BadRequestException("An id is required in the URL path.");

        return planRepository.getOne(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Plan updatePlan(Plan plan) throws BadRequestException {
        checkPlan(plan);

        planRepository.saveAndFlush(plan);
        return plan;
    }

    private void checkPlan(Plan plan) throws BadRequestException {
        if(plan.getName() == null)
            throw new BadRequestException("A name is required for the plan");

        if(plan.isLimited() && (plan.getStartDate() == null || plan.getEndDate() == null))
            throw new BadRequestException("Plan is limited therefore must have start and end dates.");
    }
}
