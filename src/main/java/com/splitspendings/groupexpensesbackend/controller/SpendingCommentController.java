package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.UpdateSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.service.SpendingCommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/spendings/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class SpendingCommentController {

    private final SpendingCommentService spendingCommentService;

    @GetMapping("/{id}")
    public SpendingCommentDto getSpendingCommentById(@PathVariable Long id) {
        return spendingCommentService.spendingCommentById(id);
    }

    @PostMapping
    public SpendingCommentDto createSpendingComment(@RequestBody NewSpendingCommentDto newSpendingCommentDto) {
        return spendingCommentService.createSpendingComment(newSpendingCommentDto);
    }

    @PatchMapping("/{id}")
    public SpendingCommentDto updateSpendingComment(@PathVariable Long id, @RequestBody UpdateSpendingCommentDto updateSpendingCommentDto) {
        return spendingCommentService.updateSpendingComment(id, updateSpendingCommentDto);
    }
}
