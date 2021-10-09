package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.UpdateSpendingCommentDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/spending-comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class SpendingCommentController {

    @GetMapping("/{id}")
    public SpendingCommentDto getSpendingCommentById(@PathVariable Long id){
        throw new UnsupportedOperationException();
        //todo
    }

    @PostMapping
    public SpendingCommentDto createSpendingComment(@RequestBody NewSpendingCommentDto newSpendingCommentDto) {
        throw new UnsupportedOperationException();
        //todo
    }

    @PatchMapping("/{id}")
    public SpendingCommentDto updateSpendingCommentInfo(@PathVariable Long id, @RequestBody UpdateSpendingCommentDto updateSpendingCommentDto) {
        throw new UnsupportedOperationException();
        //todo
    }
}
