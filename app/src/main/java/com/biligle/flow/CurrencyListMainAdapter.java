package com.biligle.flow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.PopupWindow;

import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangguoli
 */
public class CurrencyListMainAdapter extends BaseAdapter {

    private Activity context;
    private List<CurrencyBeanResult.CurrencyName> datas;
    private OnTagClickListener listener;
    private MainCurrencyPopupWindow mPop;//小弹窗
    private CurrencyPopupWindow mPop2;//更多弹窗
    private String code;

    public CurrencyListMainAdapter(MainCurrencyPopupWindow mPop,
                                   CurrencyPopupWindow mPop2,
                                   String code,
                                   final Activity context,
                                   List<CurrencyBeanResult.CurrencyName> datas) {
        this.context = context;
        this.datas = datas;
        this.mPop = mPop;
        this.mPop2 = mPop2;
        this.code = code;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_currency_main_item, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //下面两行注意顺序
        init(pos, holder);
        onListener(pos, holder);
        return convertView;
    }

    /**
     * 延迟关闭弹窗
     */
    private void dissmissDelay(View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPop.mPop != null) {
                    mPop.mPop.dismiss();
                }
            }
        }, 100);
    }

    /**
     * 初始化
     */
    @SuppressLint("SetTextI18n")
    private void init(int pos, ViewHolder holder) {
        if (datas != null && datas.get(pos) != null) {
            lineControl(pos, holder);
            holder.checkedTextView.setText(datas.get(pos).CURRENCY_NAME + " " + datas.get(pos).CURRENCY_CODE);
            holder.checkedTextView.setChecked(datas.get(pos).isChecked);
        }
    }

    /**
     * 分割线显示控制
     */
    private void lineControl(int pos, ViewHolder holder) {
        if (datas.get(pos).isGone) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        if (pos == datas.size() - 1) {
            holder.line.setVisibility(View.GONE);
        }
    }

    /**
     * 点击监听
     */
    private void onListener(final int pos, final ViewHolder holder) {
        holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && datas != null) {
                    update(pos);
                    if (pos == datas.size() - 1) {
                        mPop.isAlpha = false;
                        go2MorePopup(holder);
                    } else {
                        mPop.isAlpha = true;
                        resolveBlank(pos);
                        listener.getTagData(datas.get(pos));
                        code = datas.get(pos).CURRENCY_CODE;//更新code
                        dissmissDelay(holder.checkedTextView);
                        mPop.backgroundAlpha(1.0f);
                    }
                }
            }
        });
    }

    /**
     * 解决弹框留白问题
     */
    private void resolveBlank(int pos) {
        if (pos == 0) {
            mPop.getRootView().setBackgroundResource(R.drawable.click1);
        } else if (pos == datas.size() - 1) {
            mPop.getRootView().setBackgroundResource(R.drawable.click2);
        } else {
            mPop.getRootView().setBackgroundResource(R.drawable.noclick);
        }
    }

    /**
     * 跳转更多
     */
    private void go2MorePopup(ViewHolder holder) {
        mPop.getRootView().setBackgroundResource(R.drawable.noclick);
        dissmissDelay(holder.checkedTextView);
        mPop2.show(code);
        mPop2.getAdapter().setListener(new CurrencyListAdapter.OnTagClickListener() {
            @Override
            public void getTagData(CurrencyBeanResult.CurrencyName data) {
                code = data.CURRENCY_CODE;//更新code
                listener.getTagData(data);
            }
        });
    }

    /**
     * 每次点击更新UI
     */
    private void update(int pos) {
        for (int index = 0; index < datas.size(); index++) {
            datas.get(index).isChecked = index == pos;
            updateIsGone(index);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新isGone标识
     */
    private void updateIsGone(int index) {
        if (!datas.get(index).isChecked) {
            datas.get(index).isGone = false;
        } else {
            datas.get(index).isGone = true;
            if (datas.get(index).isMiddle) {
                datas.get(index - 1).isGone = true;
            }
        }
    }

    class ViewHolder {
        @ViewInject(R.id.tv_currency_name)
        CheckedTextView checkedTextView;//人民币 CNY

        @ViewInject(R.id.line_currency)
        View line;
    }

    public void setListener(OnTagClickListener listener) {
        this.listener = listener;
    }

    /**
     * 回调接口
     */
    public interface OnTagClickListener {
        /**
         * 选中币种回调接口
         *
         * @param data 币种+简称+ID
         */
        void getTagData(CurrencyBeanResult.CurrencyName data);
    }
}
