package com.crusoe.fo.usercenter.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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