package com.siku.storz.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ManagerDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
}
