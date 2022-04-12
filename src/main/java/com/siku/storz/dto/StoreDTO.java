package com.siku.storz.dto;

import com.siku.storz.model.Manager;
import com.siku.storz.model.Section;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class StoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String location;
    private Set<ManagerDTO> storeManagers;

}