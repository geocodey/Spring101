package com.siku.storz.dto;

import com.siku.storz.model.Section;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private double price;

}
