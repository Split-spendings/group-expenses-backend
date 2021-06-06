package com.splitspendings.groupexpensesbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "\"Group\"")
class Group implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private AppUser owner;

    @Column(nullable = false)
    private boolean personal;

    @Column(nullable = false)
    private LocalDateTime dateTimeOpened;

    @Column
    private LocalDateTime dateTimeClosed;

    @Column
    @Basic(fetch = FetchType.LAZY)
    private byte[] avatar;

    @Column
    private String inviteOption;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<GroupAppUser> memberList;




}
