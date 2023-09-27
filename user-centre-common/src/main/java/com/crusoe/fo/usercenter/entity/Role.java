package com.crusoe.fo.usercenter.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.core.serializer.Serializer;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "t_role")
@Transactional(readOnly = true)
public class Role implements Serializable {
    private static final long serialVersionUID = -8478114427891717226L;
    @Id
    @GeneratedValue
    private long id;
    private String rolename;
    private Long ordinal;

    @JsonIgnore
    @ManyToMany(mappedBy = "roleList")
    // mappedBy的参数是User类里面的被ManyToMany注释的属性名
    private List<User> userList;
    

}
