package com.xuexiang.xuidemo.fragment.components.dialog;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xui.widget.dialog.bottomsheet.XUIBottomSheet;
import com.xuexiang.xui.widget.dialog.bottomsheet.XUIBottomSheetItemView;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/11/30 下午3:18
 */
@Page(name = "BottomSheetDialog\n底部弹出窗")
public class BottomSheetFragment extends XPageSimpleListFragment {
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
        new XUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("Item 1")
                .addItem("Item 2")
                .addItem("Item 3")
                .setOnSheetItemClickListener(new XUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(XUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        ToastUtils.toast("Item " + (position + 1));
                    }
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
        XUIBottomSheet.BottomGridSheetBuilder builder = new XUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder
                .addItem(R.drawable.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, XUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, XUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_weibo, "分享到微博", TAG_SHARE_WEIBO, XUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_share_chat, "分享到私信", TAG_SHARE_CHAT, XUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_operation_save, "保存到本地", TAG_SHARE_LOCAL, XUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener(new XUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(XUIBottomSheet dialog, XUIBottomSheetItemView itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        ToastUtils.toast("tag:" + tag + ", content:" + itemView.toString());
                    }
                }).build().show();


    }

}
