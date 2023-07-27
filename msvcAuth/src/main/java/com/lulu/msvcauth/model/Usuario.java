package com.lulu.msvcauth.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class Usuario {

    private Long id;
    private String email;
    private String password;

}