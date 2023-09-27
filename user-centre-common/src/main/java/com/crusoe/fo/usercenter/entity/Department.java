package com.crusoe.fo.usercenter.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "t_department")
public class Department {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Long ordinal;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Department parent;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private List<Department> children=new ArrayList<Department>();

    @JsonBackReference
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "department")
    private List<User> userList=new ArrayList<User>();

}