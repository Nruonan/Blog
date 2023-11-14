package com.nruonan.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class HotArticleVo {
    @TableId
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;

}
