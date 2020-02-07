package com.crusoe.fo.oauth.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.core.serializer.Serializer;

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
public class Role implements Serializable {
    private static final long serialVersionUID = -8478114427891717226L;
    @Id
    @GeneratedValue
    private long id;
    private String rolename;

    @JsonIgnore
    @ManyToMany(mappedBy = "roleList")
    // mappedBy的参数是User类里面的被ManyToMany注释的属性名
    private List<User> userList;
    

}
