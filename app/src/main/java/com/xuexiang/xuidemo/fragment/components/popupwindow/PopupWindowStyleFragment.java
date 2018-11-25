package com.xuexiang.xuidemo.fragment.components.popupwindow;

import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.display.DensityUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.OnClick;

/**
 * @author xuexiang
 * @date 2017/11/12 下午4:34
 */
@Page(name = "弹出框统一样式")
public class PopupWindowStyleFragment extends BaseFragment {
    private XUISimplePopup mListPopup;
    private XUISimplePopup mMenuPopup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_popupwindow_style;
    }

    @Override
    protected void initViews() {
        initListPopup();
        initMenuPopup();
    }

    @Override
    protected void initListeners() {

    }

    private void initListPopup() {
        mListPopup = new XUISimplePopup(getContext(), DemoDataProvider.dpiItems)
                .create(DensityUtils.dip2px(getContext(), 170), new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        ToastUtils.toast(item.getTitle().toString());
                    }
                })
                .setHasDivider(true);
    }

    private void initMenuPopup() {
        mMenuPopup = new XUISimplePopup(getContext(), DemoDataProvider.menuItems)
                .create(new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        ToastUtils.toast(item.getTitle().toString());
                    }
                });
    }

    @OnClick({R.id.btn_commonlist_popup, R.id.btn_menu_popup})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commonlist_popup:
                mListPopup.showDown(v);
                break;
            case R.id.btn_menu_popup:
                mMenuPopup.showDown(v);
                break;
            default:
                break;
        }
    }


}
