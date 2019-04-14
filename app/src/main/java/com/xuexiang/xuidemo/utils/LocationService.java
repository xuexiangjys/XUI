package com.xuexiang.xuidemo.utils;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xui.logs.UILog;

import static com.xuexiang.xaop.consts.PermissionConsts.LOCATION;

/**
 * 定位服务
 *
 * @author xuexiang
 * @since 2019/3/31 下午5:52
 */
public class LocationService {

    private static volatile LocationService sInstance = null;

    private LocationClient mClient = null;
    private LocationClientOption mOption, mDIYOption;

    private LocationService() {

    }

    /***
     * 初始化
     * @param context
     */
    public void init(Context context) {
        if (mClient == null) {
            mClient = new LocationClient(context.getApplicationContext());
            mClient.setLocOption(getDefaultLocationClientOption());
        }
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static LocationService get() {
        if (sInstance == null) {
            synchronized (LocationService.class) {
                if (sInstance == null) {
                    sInstance = new LocationService();
                }
            }
        }
        return sInstance;
    }

    /***
     * 注册定位监听
     * @param listener
     * @return
     */

    public LocationService registerListener(BDAbstractLocationListener listener) {
        if (listener != null) {
            mClient.registerLocationListener(listener);
        }
        return this;
    }

    /**
     * 注销定位监听
     *
     * @param listener
     */
    public LocationService unregisterListener(BDAbstractLocationListener listener) {
        if (listener != null) {
            mClient.unRegisterLocationListener(listener);
        }
        return this;
    }

    /***
     * 设置定位参数
     * @param option
     * @return
     */
    public boolean setLocationOption(LocationClientOption option) {
        if (option != null) {
            if (mClient.isStarted()) {
                mClient.stop();
            }
            mDIYOption = option;
            mClient.setLocOption(option);
            return true;
        }
        return false;
    }

    /***
     *
     * @return DefaultLocationClientOption  默认O设置
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用

        }
        return mOption;
    }


    /**
     * @return DIYOption 自定义Option设置
     */
    public LocationClientOption getOption() {
        if (mDIYOption == null) {
            mDIYOption = new LocationClientOption();
        }
        return mDIYOption;
    }

    /**
     * 开始定位
     *
     * @param listener
     */
    @Permission(LOCATION)
    public static void start(BDAbstractLocationListener listener) {
        get().registerListener(listener).start();
    }

    /**
     * 开始定位
     */
    public LocationService start() {
        if (mClient != null && !mClient.isStarted()) {
            mClient.start();
        }
        return this;
    }

    /**
     * 停止定位
     */
    public static void stop(BDAbstractLocationListener listener) {
        get().unregisterListener(listener).stop();
    }

    /**
     * 停止定位
     */
    public LocationService stop() {
        if (mClient != null && mClient.isStarted()) {
            mClient.stop();
        }
        return this;
    }

    public boolean isStart() {
        return mClient.isStarted();
    }

    public boolean requestHotSpotState() {
        return mClient.requestHotSpotState();
    }


    /**
     * 打印地址信息
     *
     * @param location
     */
    public static void printLocationInfo(BDLocation location) {
        if (null != location && location.getLocType() != BDLocation.TypeServerError) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            /**
             * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
             * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
             */
            sb.append(location.getTime());
            sb.append("\nlocType : ");// 定位类型
            sb.append(location.getLocType());
            sb.append("\nlocType description : ");// *****对应的定位类型说明*****
            sb.append(location.getLocTypeDescription());
            sb.append("\nlatitude : ");// 纬度
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");// 经度
            sb.append(location.getLongitude());
            sb.append("\nradius : ");// 半径
            sb.append(location.getRadius());
            sb.append("\nCountryCode : ");// 国家码
            sb.append(location.getCountryCode());
            sb.append("\nCountry : ");// 国家名称
            sb.append(location.getCountry());
            sb.append("\ncitycode : ");// 城市编码
            sb.append(location.getCityCode());
            sb.append("\ncity : ");// 城市
            sb.append(location.getCity());
            sb.append("\nDistrict : ");// 区
            sb.append(location.getDistrict());
            sb.append("\nStreet : ");// 街道
            sb.append(location.getStreet());
            sb.append("\naddr : ");// 地址信息
            sb.append(location.getAddrStr());
            sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
            sb.append(location.getUserIndoorState());
            sb.append("\nDirection(not all devices have value): ");
            sb.append(location.getDirection());// 方向
            sb.append("\nlocationdescribe: ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            sb.append("\nPoi: ");// POI信息
            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                for (int i = 0; i < location.getPoiList().size(); i++) {
                    Poi poi = location.getPoiList().get(i);
                    sb.append(poi.getName() + ";");
                }
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 速度 单位：km/h
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());// 卫星数目
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 海拔高度 单位：米
                sb.append("\ngps status : ");
                sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                // 运营商信息
                if (location.hasAltitude()) {// *****如果有海拔高度*****
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                }
                sb.append("\noperationers : ");// 运营商信息
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            UILog.i(sb.toString());
        }
    }


}
