package com.xuexiang.xui.widget.spinner.editspinner;

/**
 * 监听输入并过滤
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:14
 */
public interface EditSpinnerFilter {
    /**
     * editText输入监听
     *
     * @param keyword 关键字
     * @return 是否找到匹配项
     */
    boolean onFilter(String keyword);
}
