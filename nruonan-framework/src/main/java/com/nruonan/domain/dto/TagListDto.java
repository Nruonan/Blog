package com.nruonan.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nruonan
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListDto {
    private String name;
    private String remark;
}
