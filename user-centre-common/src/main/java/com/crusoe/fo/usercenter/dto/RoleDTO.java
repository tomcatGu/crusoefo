package com.crusoe.fo.usercenter.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RoleDTO implements Serializable {
    private long id;
    private String rolename;
    private List<UserDTO> userList;

}
