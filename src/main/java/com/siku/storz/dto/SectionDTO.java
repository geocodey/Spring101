package com.siku.storz.dto;

import com.siku.storz.model.Store;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SectionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private StoreDTO store;
}
