package com.lwy.dbindingview.bindingadapter.edittext;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;

/**
 * Created by lwy on 2018/8/22.
 */

@InverseBindingMethods({
        @InverseBindingMethod(type = BindingEditText.class, attribute = "textInt", method = "getTextInt"),
        @InverseBindingMethod(type = BindingEditText.class, attribute = "textLong", method = "getTextLong"),
        @InverseBindingMethod(type = BindingEditText.class, attribute = "textFloat", method = "getTextFloat"),
        @InverseBindingMethod(type = BindingEditText.class, attribute = "textDouble", method = "getTextDouble")
})
public class BindingEditText extends AppCompatEditText {

    public BindingEditText(Context context) {
        super(context);
    }

    public BindingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isNeedConvert(String text) {
        if (!"-".equals(text) && !TextUtils.isEmpty(text))
            return true;
        else
            return false;
    }

    public Integer getTextInt() {
        Integer retInt = null;
        String text = getText().toString();
        if (isNeedConvert(text)) {
            retInt = Integer.parseInt(text);
        }
        return retInt;
    }

    public Long getTextLong() {
        Long retLong = null;
        String text = getText().toString();
        if (isNeedConvert(text)) {
            retLong = Long.parseLong(text);
        }
        return retLong;
    }

    public Double getTextDouble() {
        Double retDouble = null;
        String text = getText().toString();
        if (isNeedConvert(text)) {
            retDouble = Double.parseDouble(text);
        }
        return retDouble;
    }

    public Float getTextFloat() {
        Float retFloat = null;
        String text = getText().toString();
        if (isNeedConvert(text)) {
            retFloat = Float.parseFloat(text);
        }
        return retFloat;
    }

    private static void initTextNumber(BindingEditText bindingEditText, Object textObj) {
        String text;
        if (textObj == null)
            text = "";
        else
            text = textObj.toString();
        String curStr = bindingEditText.getText().toString();
        String temp = curStr + ".0";
        // temp.equals(temp) -> eg: 文本框为6 返回的Double为6.0  排除这种情况
//        if (!TextUtils.isEmpty(curStr) && (curStr.endsWith(".") || text.equals(temp)))
//            return;
//        对"-"的判断主要是为了做负数的输入做兼容
        if (curStr.endsWith(".") || curStr.startsWith(".") || text.equals(temp)) {
            return;
        }
        if (!"-".equals(curStr) && !curStr.equals(text))
            bindingEditText.setText(text);
    }

    @BindingAdapter("textInt")
    public static void initTextInt(BindingEditText bindingEditText, Integer textInt) {
        initTextNumber(bindingEditText, textInt);
    }

    @BindingAdapter("textLong")
    public static void initTextLong(BindingEditText bindingEditText, Long textLong) {
        initTextNumber(bindingEditText, textLong);
    }

    @BindingAdapter("textDouble")
    public static void initTextDouble(BindingEditText bindingEditText, Double textDouble) {
        initTextNumber(bindingEditText, textDouble);
    }

    @BindingAdapter("textFloat")
    public static void initTextFloat(BindingEditText bindingEditText, Float textFloat) {
        initTextNumber(bindingEditText, textFloat);
    }

    @BindingAdapter("textIntAttrChanged")
    public static void setIntValueChangedListener(BindingEditText bindingEditText, InverseBindingListener inverseBindingListener) {
        bindingEditText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
        bindingEditText.addTextChangedListener(new CustomTextWatcher(bindingEditText, inverseBindingListener, new IntegerJudgeStrategy()));
    }

    @BindingAdapter("textLongAttrChanged")
    public static void setLongValueChangedListener(BindingEditText bindingEditText, InverseBindingListener inverseBindingListener) {
        bindingEditText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
        bindingEditText.addTextChangedListener(new CustomTextWatcher(bindingEditText, inverseBindingListener, new LongJudgeStrategy()));
    }

    @BindingAdapter("textDoubleAttrChanged")
    public static void setDoubleValueChangedListener(BindingEditText bindingEditText, final InverseBindingListener inverseBindingListener) {
        bindingEditText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
        bindingEditText.addTextChangedListener(new CustomTextWatcher(bindingEditText, inverseBindingListener, new DoubleJudgeStrategy()));
    }

    @BindingAdapter("textFloatAttrChanged")
    public static void setFloatValueChangedListener(BindingEditText bindingEditText, InverseBindingListener inverseBindingListener) {
        bindingEditText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
        bindingEditText.addTextChangedListener(new CustomTextWatcher(bindingEditText, inverseBindingListener, new FloatJudgeStrategy()));
    }

    public static class CustomTextWatcher implements TextWatcher {

        private final BindingEditText bindingEditText;
        private final InverseBindingListener inverseBindingListener;
        private final NumberJudgeStrategy numberJudgeStrategy;

        public CustomTextWatcher(BindingEditText bindingEditText, InverseBindingListener inverseBindingListener,
                                 NumberJudgeStrategy numberJudgeStrategy) {
            this.bindingEditText = bindingEditText;
            this.inverseBindingListener = inverseBindingListener;
            this.numberJudgeStrategy = numberJudgeStrategy;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if ("-".equals(text) || TextUtils.isEmpty(text)) {
                if (this.inverseBindingListener != null) {
                    this.inverseBindingListener.onChange();
                }
                return;
            }
            if (numberJudgeStrategy.isMatch(text)) {
                if (this.inverseBindingListener != null) {
                    this.inverseBindingListener.onChange();
                }
//                bindingEditText.setSelection(bindingEditText.getEditableText().toString().length());
            } else {
                String newStr = text.substring(0, text.length() - 1);
                bindingEditText.setText(newStr);
                bindingEditText.setSelection(newStr.length());
            }
        }
    }

    public static class IntegerJudgeStrategy implements NumberJudgeStrategy {
        @Override
        public boolean isMatch(String text) {
            boolean flag = false;
            try {
                Integer.parseInt(text);
                flag = true;
            } catch (NumberFormatException e) {

            }
            return flag;
        }
    }

    public static class LongJudgeStrategy implements NumberJudgeStrategy {
        @Override
        public boolean isMatch(String text) {
            boolean flag = false;
            try {
                Long.parseLong(text);
                flag = true;
            } catch (NumberFormatException e) {

            }
            return flag;
        }
    }

    public static class FloatJudgeStrategy implements NumberJudgeStrategy {
        @Override
        public boolean isMatch(String text) {
            boolean flag = false;
            try {
                Float.parseFloat(text);
                flag = true;
            } catch (NumberFormatException e) {

            }
            return flag;
        }
    }

    public static class DoubleJudgeStrategy implements NumberJudgeStrategy {
        @Override
        public boolean isMatch(String text) {
            boolean flag = false;
            try {
                Double.parseDouble(text);
                flag = true;
            } catch (NumberFormatException e) {

            }
            return flag;
        }
    }

    public interface NumberJudgeStrategy {
        boolean isMatch(String text);
    }
}
