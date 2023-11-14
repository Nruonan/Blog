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
public class EditTagDto {
    private Long id;
    //备注
    private String remark;
    //标签名
    private String name;
}
