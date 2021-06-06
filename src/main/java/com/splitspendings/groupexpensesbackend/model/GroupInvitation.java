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
public class GroupInvitation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="app_user_to_id", nullable=false)
    private AppUser to;


    @ManyToOne
    @JoinColumn(name="group_app_user_id", nullable=false)
    private GroupAppUser groupAppUser;

    @Column(nullable = false)
    private LocalDateTime dateTimeCreated;

    @Column
    private String message;

}
