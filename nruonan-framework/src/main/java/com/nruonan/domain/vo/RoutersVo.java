package com.nruonan.domain.vo;

import com.nruonan.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@Data
@AllArgsConstructor
public class RoutersVo {
    private List<Menu> menus;
}
