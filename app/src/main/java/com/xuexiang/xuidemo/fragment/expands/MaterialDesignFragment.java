package com.xuexiang.xuidemo.fragment.expands;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.activity.MaterialDesignThemeActivity;
import com.xuexiang.xuidemo.activity.SettingsActivity;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.BadgeDrawableFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.BehaviorFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.BottomSheetDialogFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.ConstraintLayoutFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.DrawerLayoutFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.ItemTouchHelperFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.MaterialButtonFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.MotionLayoutFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.ShapeableImageViewFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.TextInputLayoutFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.ToolBarFragment;
import com.xuexiang.xutil.app.ActivityUtils;

import java.util.List;

import static com.xuexiang.xuidemo.base.BaseActivity.KEY_SUPPORT_SLIDE_BACK;

import android.os.Build;

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
        lists.add("ConstraintLayout\n约束布局");
        lists.add("MotionLayout\n能实现丰富的交互动画");
        lists.add("ItemTouchHelper+RecyclerView\n实现列表拖拽");
        lists.add("AppCompatPreferenceActivity\n设置页面");
        lists.add("BottomSheetDialog");
        lists.add("BadgeDrawable");
        lists.add("ShapeableImageView");
        lists.add("MaterialButton");
        lists.add("TextInputLayout");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch (position) {
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
                openPage(ConstraintLayoutFragment.class);
                break;
            case 4:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    XToastUtils.warning("当前手机版本过低，暂不支持");
                } else {
                    openPage(MotionLayoutFragment.class);
                }
                break;
            case 5:
                openPage(ItemTouchHelperFragment.class);
                break;
            case 6:
                ActivityUtils.startActivity(SettingsActivity.class);
                break;
            case 7:
                openPage(BottomSheetDialogFragment.class);
                break;
            case 8:
                PageOption.to(BadgeDrawableFragment.class)
                        .setNewActivity(true, MaterialDesignThemeActivity.class)
                        .open(this);
                break;
            case 9:
                openPage(ShapeableImageViewFragment.class);
                break;
            case 10:
                PageOption.to(MaterialButtonFragment.class)
                        .setNewActivity(true, MaterialDesignThemeActivity.class)
                        .open(this);
                break;
            case 11:
                openPage(TextInputLayoutFragment.class);
                break;
            default:
                break;
        }
    }
}
