package com.biligle.flow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @Author wangguoli
 */
public class CurrencyListAdapter extends BaseAdapter {

    private Activity context;
    private List<CurrencyBeanResult.CurrencyDetails> datas;
    private TagAdapter<CurrencyBeanResult.CurrencyName> adapter;
    private OnTagClickListener listener;
    private PopupWindow mPop;
    private int width;

    public CurrencyListAdapter(PopupWindow mPop, final Activity context, List<CurrencyBeanResult.CurrencyDetails> datas) {
        this.context = context;
        this.datas = datas;
        this.mPop = mPop;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        width = (display.getWidth() - SizeUtils.dp2px(64f)) / 3;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_currency_list_item, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //下面两行注意顺序
        setData(pos, holder);
        onListener(pos, holder);
        return convertView;
    }

    /**
     * 配置数据
     *
     * @param itemPosition 当前item下标（热门、亚洲、非洲......）
     * @param holder       封装的item.xml
     */
    private void setData(final int itemPosition, final ViewHolder holder) {
        if (datas == null || datas.get(itemPosition) == null || datas.get(itemPosition).data == null)
            return;
        holder.tv_tag.setText(datas.get(itemPosition).tag);
        adapter = new TagAdapter<CurrencyBeanResult.CurrencyName>(datas.get(itemPosition).data) {
            @SuppressLint("SetTextI18n")
            @Override
            public View getView(FlowLayout parent, int position, CurrencyBeanResult.CurrencyName currencyName) {
                final TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv,
                        holder.mFlowLayout, false);
                if (currencyName != null) {
                    tv.setText(currencyName.CURRENCY_NAME + " " + currencyName.CURRENCY_CODE);
                }
                if (tv.getText().length() <= 8) {
                    tv.setWidth(width);
                } else if (tv.getText().length() > 8) {
                    int maxWidth = width / 8 *tv.getText().length() > 2 * width ? 2 * width : width / 7 *tv.getText().length();
                    tv.setWidth(maxWidth);
                    int right = 2 * width + SizeUtils.dp2px(12f) - maxWidth;
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(SizeUtils.dp2px(12f), SizeUtils.dp2px(12f), right, 0);//4个参数按顺序分别是左上右下
                    tv.setLayoutParams(layoutParams);
                }
                return tv;
            }
        };
        holder.mFlowLayout.setAdapter(adapter);
        initCheckedState(itemPosition, holder);
    }

    /**
     * TagView监听（币种）
     *
     * @param itemPosition item下标
     * @param holder       封装的item.xml
     */
    private void onListener(final int itemPosition, final ViewHolder holder) {
        holder.mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                updateCheckedState(view, position, itemPosition);
                if (listener != null
                        && datas.get(itemPosition) != null
                        && datas.get(itemPosition).data != null) {
                    listener.getTagData(datas.get(itemPosition).data.get(position));
                    dissmissDelay(holder.tv_tag);
                }
                return true;
            }
        });
    }

    /**
     * 延迟关闭弹窗
     */
    private void dissmissDelay(View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPop.dismiss();
            }
        }, 100);
    }

    /**
     * 初始化选中状态（必须在setAdapter之后）
     *
     * @param itemPosition item的下标
     * @param holder       封装的item.xml
     */
    private void initCheckedState(int itemPosition, ViewHolder holder) {
        int childCount = holder.mFlowLayout.getChildCount();
        if (childCount > 0) {
            for (int index = 0; index < childCount; index++) {
                ((TagView) holder.mFlowLayout.getChildAt(index))
                        .setChecked(datas.get(itemPosition).data.get(index).isChecked);
            }
        }
    }

    /**
     * 更新选中状态（当前需求：只要币种相同，如果选中一个，其他分类中也会选中,反之，则全不选中）
     *
     * @param view         当前的币种
     * @param position     当前币种的下标
     * @param itemPosition 当前item下标（热门、亚洲、非洲......）
     */
    private void updateCheckedState(View view, int position, int itemPosition) {
        //当前Item中，选中的币种CODE
        String currentCode = datas.get(itemPosition).data.get(position).CURRENCY_CODE;
        //循环Item（非洲、亚洲......）
        for (int itemIndex = 0; itemIndex < datas.size(); itemIndex++) {
            //循环币种
            for (int index = 0; index < datas.get(itemIndex).data.size(); index++) {
                String otherCode = datas.get(itemIndex).data.get(index).CURRENCY_CODE;
                datas.get(itemIndex).data.get(index).isChecked = currentCode.equals(otherCode);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 注入监听
     *
     * @param listener 币种监听
     */
    public void setListener(OnTagClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_tag)
        TextView tv_tag;//热门、亚洲、非洲......

        @ViewInject(R.id.id_flowlayout)
        TagFlowLayout mFlowLayout;//人民币 CNY......
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
