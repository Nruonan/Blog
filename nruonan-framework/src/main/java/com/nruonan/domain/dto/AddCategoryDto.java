package com.nruonan.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nruonan
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCategoryDto {
    private String name;
    private String description;
    private String status;
}
