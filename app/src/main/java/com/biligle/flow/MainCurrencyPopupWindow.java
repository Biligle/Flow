package com.biligle.flow;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangguoli
 */
public class MainCurrencyPopupWindow extends PopupWindow {
    private Activity mActivity;
    public PopupWindow mPop;
    private View rootView;
    private List<CurrencyBeanResult.CurrencyName> datas;
    private CurrencyListMainAdapter adapter;
    private String code;
    private CurrencyPopupWindow currencyPopupWindow;
    public boolean isAlpha = true;//true：透明度1.0f
    private int width;


    public MainCurrencyPopupWindow(Activity mActivity, String code) {
        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        this.mActivity = mActivity;
        this.code = code;
        if (mPop == null) {
            init();
        }
    }

    private void init() {
        rootView = View.inflate(mActivity,
                R.layout.view_currency_main_pop, null);
        mPop = new PopupWindow(rootView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPop.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        mPop.setTouchable(true);
        rootView.setFocusable(true);
        mPop.setOnDismissListener(new poponDismissListener());
        final ListView listView = rootView.findViewById(R.id.currency_main_list);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        //获取全部币种
        currencyPopupWindow = new CurrencyPopupWindow(mActivity, this);
        initData();
        adapter = new CurrencyListMainAdapter(this, currencyPopupWindow, code, mActivity, datas);
        listView.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String json = "{\"head\":{\"CODE\":\"0\"},\"data\":{\"list\":[{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0}],\"tag\":\"热门币种\"},{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\"}],\"tag\":\"亚洲\"},{\"data\":[{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\"}],\"tag\":\"非洲\"},{\"data\":[{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\"}],\"tag\":\"大洋洲\"}],\"TYPE\":\"GET_PRD_RANK_HOME_PAGE_CURRENCY\",\"home\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"阿拉伯元\",\"ID\":44,\"CURRENCY_CODE\":\"ALB\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"因吹思听元\",\"ID\":47,\"CURRENCY_CODE\":\"INTRESTING\",\"CURRENCY_INDEX\":0}]}}";
        CurrencyBeanResult currencyBeanResult = new Gson().fromJson(json, CurrencyBeanResult.class);
        datas = currencyBeanResult.data.home;
        List<CurrencyBeanResult.CurrencyName> homeData = new ArrayList<>();
        for (int i = 0; i < datas.size() && i < 6; i++) {
            homeData.add(datas.get(i));
        }
        if (datas.size() <= 6) {
            CurrencyBeanResult.CurrencyName name = new CurrencyBeanResult.CurrencyName();
            name.CURRENCY_NAME = "更多";
            name.CURRENCY_CODE = "";
            homeData.add(name);
        } else {
            CurrencyBeanResult.CurrencyName name = new CurrencyBeanResult.CurrencyName();
            name.CURRENCY_NAME = "更多(" + datas.get(6).CURRENCY_NAME + "…)";
            name.CURRENCY_CODE = "";
            homeData.add(name);
        }
        datas = homeData;
    }


    /**
     * 获取根布局
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 获取Adapter
     */
    public CurrencyListMainAdapter getAdapter() {
        return adapter;
    }

    /**
     * 更多弹窗
     */
    public CurrencyPopupWindow getCurrencyPop() {
        return currencyPopupWindow;
    }

    public void show(String code, View view) {
        if (mPop != null && !mPop.isShowing()) {
            defaultCheckedState(code);
            backgroundAlpha(0.5f);
            if (Build.VERSION.SDK_INT == 24) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                mPop.showAtLocation(view, Gravity.TOP, width / 2 - getWidth() / 2 - SizeUtils.px2dp(15f), y + view.getHeight());
            } else {
                mPop.showAsDropDown(view, -view.getWidth() - SizeUtils.px2dp(15f), 0);
            }
            updateDatas(code);
        }
    }

    /**
     * show之后，拉取最新币种数据
     */
    private void updateDatas(String code) {
        //以下是模拟访问网络
        final String str = code;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getLatestData();
                defaultCheckedState(str);
            }
        }, 2000);
    }

    /**
     * 获取最新数据，更新popupwindow
     */
    private void getLatestData() {
        String json = "{\"head\":{\"CODE\":\"0\"},\"data\":{\"list\":[{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0}],\"tag\":\"热门币种\"},{\"data\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\"},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\"},{\"CURRENCY_NAME\":\"人民币\",\"ID\":57,\"CURRENCY_CODE\":\"CNY\"}],\"tag\":\"亚洲\"},{\"data\":[{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\"}],\"tag\":\"非洲\"},{\"data\":[{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\"}],\"tag\":\"大洋洲\"}],\"TYPE\":\"GET_PRD_RANK_HOME_PAGE_CURRENCY\",\"home\":[{\"CURRENCY_NAME\":\"人民币\",\"ID\":49,\"CURRENCY_CODE\":\"CNY\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"港币\",\"ID\":53,\"CURRENCY_CODE\":\"HKD\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"testtttt\",\"ID\":64,\"CURRENCY_CODE\":\"testtest\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"文莱元\",\"ID\":45,\"CURRENCY_CODE\":\"BND\",\"CURRENCY_INDEX\":0},{\"CURRENCY_NAME\":\"阿拉伯元\",\"ID\":44,\"CURRENCY_CODE\":\"ALB\",\"CURRENCY_INDEX\":0}]}}";
        CurrencyBeanResult result = new Gson().fromJson(json, CurrencyBeanResult.class);
        CurrencyBeanResult.CurrencyBean bean = result.data;
        List<CurrencyBeanResult.CurrencyName> homeData = new ArrayList<>();
        for (int i = 0; i < bean.home.size() && i < 6; i++) {
            homeData.add(bean.home.get(i));
        }
        if (bean.home.size() > 1) {
            if (bean.home.size() <= 6) {
                CurrencyBeanResult.CurrencyName name = new CurrencyBeanResult.CurrencyName();
                name.CURRENCY_NAME = "更多";
                name.CURRENCY_CODE = "";
                homeData.add(name);
            } else {
                CurrencyBeanResult.CurrencyName name = new CurrencyBeanResult.CurrencyName();
                name.CURRENCY_NAME = "更多(" + bean.home.get(6).CURRENCY_NAME + "…)";
                name.CURRENCY_CODE = "";
                homeData.add(name);
            }
        }
        datas.clear();
        datas.addAll(homeData);
    }

    /**
     * 默认选中币种
     *
     * @param code 币种简称
     */
    private void defaultCheckedState(String code) {
        initIsMiddle(datas);
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).isChecked = code.equals(datas.get(i).CURRENCY_CODE);
            initIsGone(i);
        }
        adapter.notifyDataSetChanged();
        if (datas.get(0).isChecked) {
            rootView.setBackgroundResource(R.drawable.click1);
        } else {
            rootView.setBackgroundResource(R.drawable.noclick);
        }
    }

    /**
     * 初始化isGone标识
     */
    private void initIsGone(int i) {
        if (!datas.get(i).isChecked) {
            datas.get(i).isGone = false;
        } else {
            datas.get(i).isGone = true;
            if (datas.get(i).isMiddle) {
                datas.get(i - 1).isGone = true;
            }
        }
    }

    /**
     * 初始化isMiddle标识
     */
    private void initIsMiddle(List<CurrencyBeanResult.CurrencyName> datas) {
        if (datas == null) return;
        if (datas.size() > 1) {
            for (int i = 0; i < datas.size(); i++) {
                if (i > 0) {
                    datas.get(i).isMiddle = true;
                } else {
                    datas.get(i).isMiddle = false;
                }
            }
        } else {
            datas.get(0).isMiddle = false;
            datas.get(0).isGone = true;
        }
    }

    /**
     * 背景透明度1.0
     */
    private class poponDismissListener implements OnDismissListener {
        @Override
        public void onDismiss() {
            //如果是“更多”点击事件，不设置1f
            if (isAlpha) {
                backgroundAlpha(1f);
            }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {

            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        mActivity.getWindow().setAttributes(lp);
    }

}
