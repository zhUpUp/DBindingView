package com.lwy.dbindingview.bindingadapter.radiogroup;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import com.lwy.dbindingview.data.KeyValue;


/**
 * Created by mac on 2017/11/3.
 */

@InverseBindingMethods({
        @InverseBindingMethod(
                type = DataBindingRadioGroup.class,
                attribute = "checkedValue",
                event = "checkedValueAttrChanged",
                method = "getCheckedValue")
})
public class DataBindingRadioGroup extends RadioGroup {

    //    private InverseBindingListener mBindingListener;
    private KeyValue checkedValue;
    //    private OnValueChangedListener listener;
    private InverseBindingListener listener;

    public DataBindingRadioGroup(Context context) {
        super(context);
        init();
    }

    public DataBindingRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId > 0) {
                    DataBindingRadioButton radioButton = (DataBindingRadioButton) findViewById(checkedId);
                    KeyValue keyValue = new KeyValue(radioButton.getValue(), radioButton.getText().toString());
                    setCheckedValue(radioButton.isChecked() ? keyValue : null);
                } else {
                    setCheckedValue(null);
                }
            }
        });
    }

    public KeyValue getCheckedValue() {
        return checkedValue;
    }

    public void setCheckedValue(KeyValue checkedValue) {
        if (checkedValue != null && this.checkedValue != null) {
            Integer inputKey = checkedValue.key;
            Integer originKey = this.checkedValue.key;
            if (isSame(originKey, inputKey)) {
                return;
            }
        }
        if (checkedValue == null && this.checkedValue == null)
            return;

        this.checkedValue = checkedValue;

        if (this.checkedValue == null) {
            clearCheck();
        } else {
            DataBindingRadioButton customRadioButton = (DataBindingRadioButton) findViewById(getCheckedRadioButtonId());
            Integer key = this.checkedValue.key;
            if (customRadioButton == null || !isSame(key, customRadioButton.getValue())) {

                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child instanceof DataBindingRadioButton) {
                        Integer value = ((DataBindingRadioButton) child).getValue();
                        if (isSame(this.checkedValue.key, value)) {
                            ((DataBindingRadioButton) child).setChecked(true);
                        }
                    }
                }
            }
        }

        if (listener != null) {
            listener.onChange();
        }
    }

    public void setListener(InverseBindingListener listener) {
        this.listener = listener;
    }

    //    public void setListener(OnValueChangedListener listener) {
//        this.listener = listener;
//    }

//    public interface OnValueChangedListener {
//        void onValueChanged();
//    }

    @BindingAdapter("checkedValueAttrChanged")
    public static void setValueChangedListener(DataBindingRadioGroup view, final InverseBindingListener bindingListener) {
        if (bindingListener == null) {
            view.setListener(null);
        } else {
            // 通知 ViewModel
            view.setListener(bindingListener);
        }
    }

    static boolean isSame(Integer value1, Integer value2) {
        return (value1 == null && value2 == null) || (value1 != null && value1.equals(value2));
    }
}