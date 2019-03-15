package com.membership.controller;

import com.membership.exception.BadRequestException;
import com.membership.exception.NotFoundException;
import com.membership.model.Member;
import com.membership.model.Plan;
import com.membership.repository.MemberRepository;
import com.membership.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/member")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PlanRepository planRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member createMember(@RequestBody Member member) throws BadRequestException {
        checkMember(member);

        memberRepository.save(member);

        return member;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Member getPlanById(@PathVariable Long id) throws BadRequestException {
        if(id == null)
            throw new BadRequestException("An id is required in the URL path.");

        return memberRepository.getOne(id);
    }

    @PutMapping("add/{memberId}/{planId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean addMemberToPlan(@PathVariable Long memberId, @PathVariable Long planId) throws NotFoundException {
        Member member = memberRepository.getOne(memberId);
        if(member == null)
            throw new NotFoundException("Member was not found.");

        Plan plan = planRepository.getOne(planId);
        if(plan == null)
            throw new NotFoundException("Plan was not found.");

        member.setPlan(plan);
        memberRepository.save(member);

        return true;
    }

    private void checkMember(Member member) throws BadRequestException {
        if(member.getFirstName() == null ||
           member.getLastName() == null ||
           member.getDateOfBirth() == null)
            throw new BadRequestException("Member's first, last name and date of birth is required.");
    }
}
