package com.xuexiang.xui.widget.spinner.editspinner;


/**
 * 监听输入并过滤
 * @author xuexiang
 * @date 2017/12/10 下午5:00
 */
public interface EditSpinnerFilter {
    /**
     * editText输入监听
     * @param keyword
     * @return
     */
    boolean onFilter(String keyword);
}
