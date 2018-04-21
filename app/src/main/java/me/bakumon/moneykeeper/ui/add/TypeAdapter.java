package me.bakumon.moneykeeper.ui.add;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import com.hhl.gridpagersnaphelper.GridPagerUtils;
import com.hhl.gridpagersnaphelper.transform.TwoRowDataTransform;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.utill.ScreenUtils;

/**
 * TypeAdapter
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */

public class TypeAdapter extends BaseDataBindingAdapter<RecordType> {

    private int mItemWidth;
    private int mColumn;
    private int mCurrentCheckPosition;

    public TypeAdapter(@Nullable List<RecordType> data, int column) {
        super(R.layout.item_type, data);
        mColumn = column;
        int screenWidth = ScreenUtils.getScreenWidth(App.getINSTANCE());
        mItemWidth = screenWidth / column;
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.recordType, item);
        binding.setVariable(BR.width, mItemWidth);
        binding.executePendingBindings();
    }

    /**
     * 筛选出支出和收入
     *
     * @param data 支出和收入总数据
     * @param type 类型 0：支出 1：收入
     * @see RecordType#TYPE_OUTLAY 支出
     * @see RecordType#TYPE_INCOME 收入
     */
    public void setNewData(@Nullable List<RecordType> data, int type) {
        if (data != null) {
            List<RecordType> result = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).type == type) {
                    result.add(data.get(i));
                }
            }
            // 适配网格分页布局
            result = GridPagerUtils.transformAndFillEmptyData(
                    new TwoRowDataTransform<>(mColumn), result);
            // 选中第一个
            super.setNewData(result);
            checkItem(0);
        } else {
            super.setNewData(null);
        }
    }

    /**
     * 选中某一个 item
     *
     * @param position 选中 item 的索引
     */
    public void checkItem(int position) {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i) != null) {
                getData().get(i).isChecked = i == position;
            }
        }
        mCurrentCheckPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 获取当前选中的 item
     */
    public RecordType getCurrentItem() {
        return getItem(mCurrentCheckPosition);
    }
}