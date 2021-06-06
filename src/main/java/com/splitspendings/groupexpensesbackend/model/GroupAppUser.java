package com.splitspendings.groupexpensesbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity


public class GroupAppUser implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name="member_id", nullable=false)
    private AppUser member;


    @ManyToOne
    @JoinColumn(name="group_id", nullable=false)
    private Group group;


    @Column(nullable = false)
    private LocalDateTime dateTimeFirstJoined;

    @Column(nullable = false)
    private LocalDateTime dateTimeLastJoined;

    @Column(nullable = false)
    private boolean hasAdminRights;

    @Column
    private LocalDateTime dateTimeLeft;

}
