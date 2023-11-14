package com.nruonan.constants;

/**
 * @author Nruonan
 * @description
 */
public class SystemConstants {
    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 文章列表当前查询页数
     */
    public static final int ARTICLE_STATUS_CURRENT = 1;

    /**
     * 文章列表每页显示的数据条数
     */
    public static final int ARTICLE_STATUS_SIZE = 10;

    /**
     * 分类表的分类状态是正常状态
     */
    public static final String STATUS_NORMAL = "0";
    /**
     * 友链状态为审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 根评论
     */
    public static final String COMMENT_ROOT = "-1";
    /**
     * 友链评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     * 文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * redis存储浏览量
     */
    public static final String ARTICLE_VIEW_COUNT ="article:viewCount";

    public static final String MENU = "C";
    public static final String BUTTON = "F";
    // 正常状态
    public static final String NORMAL = "0";
    // 后台管理员
    public static final String ADMIN = "1";
}
