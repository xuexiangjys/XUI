package com.xuexiang.xuidemo.fragment.components.dialog;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xui.utils.XToastUtils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/11/30 下午3:18
 */
@Page(name = "BottomSheetDialog\n底部弹出窗")
public class BottomSheetFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("BottomSheet List");
        lists.add("BottomSheet Grid");
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
                showSimpleBottomSheetList();
                break;
            case 1:
                showSimpleBottomSheetGrid();
                break;
            default:
                break;
        }
    }

    // ================================ 生成不同类型的BottomSheet
    private void showSimpleBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .setTitle("标题")
                .addItem("Item 1")
                .addItem("Item 2")
                .addItem("Item 3")
                .setIsCenter(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    XToastUtils.toast("Item " + (position + 1));
                })
                .build()
                .show();
    }

    private void showSimpleBottomSheetGrid() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        final int TAG_SHARE_WEIBO = 2;
        final int TAG_SHARE_CHAT = 3;
        final int TAG_SHARE_LOCAL = 4;
        BottomSheet.BottomGridSheetBuilder builder = new BottomSheet.BottomGridSheetBuilder(getActivity());
        builder
                .addItem(R.drawable.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_weibo, "分享到微博", TAG_SHARE_WEIBO, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_chat, "分享到私信", TAG_SHARE_CHAT, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_save, "保存到本地", TAG_SHARE_LOCAL, BottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener((dialog, itemView) -> {
                    dialog.dismiss();
                    int tag = (int) itemView.getTag();
                    XToastUtils.toast("tag:" + tag + ", content:" + itemView.toString());
                }).build().show();


    }

}
