package com.xuexiang.xuidemo.fragment.expands;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.activity.SettingsActivity;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.BehaviorFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.BottomSheetDialogFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.DrawerLayoutFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.ToolBarFragment;
import com.xuexiang.xutil.app.ActivityUtils;

import java.util.List;

import static com.xuexiang.xuidemo.base.BaseActivity.KEY_SUPPORT_SLIDE_BACK;

/**
 * @author xuexiang
 * @since 2019-05-07 23:30
 */
@Page(name = "Material Design", extra = R.drawable.ic_expand_material_design)
public class MaterialDesignFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("ToolBar使用");
        lists.add("Behavior\n手势行为");
        lists.add("DrawerLayout + NavigationView\n常见主页布局");
        lists.add("BottomSheetDialog");
        lists.add("设置页面");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                openPage(ToolBarFragment.class);
                break;
            case 1:
                openPage(BehaviorFragment.class);
                break;
            case 2:
                openNewPage(DrawerLayoutFragment.class, KEY_SUPPORT_SLIDE_BACK, false);
                break;
            case 3:
                openPage(BottomSheetDialogFragment.class);
                break;
            case 4:
                ActivityUtils.startActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }
}
