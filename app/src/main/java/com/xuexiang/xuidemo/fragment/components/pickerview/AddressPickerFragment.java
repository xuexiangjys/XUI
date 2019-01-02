/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xuidemo.fragment.components.pickerview;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.resource.ResourceUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019/1/1 下午10:46
 */
@Page(name = "OptionsPicker\n条件选择器--省市区三级联动")
public class AddressPickerFragment extends BaseFragment {

    @BindView(R.id.btn_load)
    Button btnLoad;
    @BindView(R.id.btn_picker)
    Button btnPicker;

    private List<ProvinceInfo> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    boolean isLoaded;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_picker;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.btn_load, R.id.btn_picker, R.id.btn_picker_dialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_load:
                loadData();
                break;
            case R.id.btn_picker:
                showPickerView(false);
                break;
            case R.id.btn_picker_dialog:
                showPickerView(true);
                break;
        }
    }

    @IOThread
    private void loadData() {//加载数据
        if (isLoaded) {
            ToastUtils.toast("已加载！");
            return;
        }

        String JsonData = ResourceUtils.readStringFromAssert("province.json");
        List<ProvinceInfo> provinceInfos = JsonUtil.fromJson(JsonData, new TypeToken<List<ProvinceInfo>>() {
        }.getType());
        if (provinceInfos == null) {
            isLoaded = false;
            ToastUtils.toast("加载失败！");
            return;
        }
        /**
         * 添加省份数据
         */
        options1Items = provinceInfos;

        for (ProvinceInfo provinceInfo : provinceInfos) { //遍历省份
            List<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            List<List<String>> areaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (ProvinceInfo.City city : provinceInfo.getCityList()) {
                String CityName = city.getName();
                cityList.add(CityName);//添加城市

                List<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (city.getArea() == null || city.getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(city.getArea());
                }
                areaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(areaList);
        }
        isLoaded = true;
        ToastUtils.toast("加载成功！");
    }


    private void showPickerView(boolean isDialog) {// 弹出选择器
        if (!isLoaded) {
            ToastUtils.toast("请先加载数据！");
            return;
        }

        int[] defaultSelectOptions = getDefaultCity();

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + "-" +
                        options2Items.get(options1).get(options2) + "-" +
                        options3Items.get(options1).get(options2).get(options3);

                ToastUtils.toast(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .isDialog(isDialog)
                .setSelectOptions(defaultSelectOptions[0], defaultSelectOptions[1], defaultSelectOptions[2])
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * @return 获取默认城市的索引
     */
    private int[] getDefaultCity() {
        int[] res = new int[3];
        ProvinceInfo provinceInfo;
        List<ProvinceInfo.City> cities;
        ProvinceInfo.City city;
        List<String> ares;
        for (int i = 0; i < options1Items.size(); i++) {
            provinceInfo = options1Items.get(i);
            if ("江苏省".equals(provinceInfo.getName())) {
                res[0] = i;
                cities = provinceInfo.getCityList();
                for (int j = 0; j < cities.size(); j++) {
                    city = cities.get(j);
                    if ("南京市".equals(city.getName())) {
                        res[1] = j;
                        ares = city.getArea();
                        for (int k = 0; k < ares.size(); k++) {
                            if ("雨花台区".equals(ares.get(k))) {
                                res[2] = k;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return res;
    }

    @Override
    public void onDestroyView() {
        options1Items.clear();
        options2Items.clear();
        options3Items.clear();
        super.onDestroyView();
    }
}
