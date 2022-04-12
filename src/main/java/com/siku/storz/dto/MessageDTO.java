package com.siku.storz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
}

