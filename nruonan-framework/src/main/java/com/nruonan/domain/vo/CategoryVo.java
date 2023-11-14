package com.nruonan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nruonan
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {
    private Long id;
    //分类名
    private String name;
    private String status;
    private String description;
}
