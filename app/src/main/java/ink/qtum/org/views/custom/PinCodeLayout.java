package ink.qtum.org.views.custom;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ink.qtum.org.inkqtum.R;
import ink.qtum.org.utils.KeyboardManager;

public class PinCodeLayout extends LinearLayout {

    private boolean isShowComma = true;
    private int pinCodeLeftRight = 24;
    private int pinCodeMarginBottom = 24;
    private Integer maxCount = 6;

    private final int VIBRATE_ERROR_TIME = 400;

    private Context context;
    private LinearLayout pinCodeLayout;

    private OnPinCodeListener listener;
    private TextView warningText;
    private View warningView;
    private String pinText = "";
    private List<EditText> editTextList = new ArrayList<>();

    public interface OnPinCodeListener {
        void onTextChanged(String text);
    }

    public PinCodeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initXmlStyle(attrs);
        init(context);
    }

    public PinCodeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initXmlStyle(attrs);
        init(context);
    }

    public PinCodeLayout(Context context) {
        super(context);
        init(context);
    }


    public void setErrorMessage(String msg) {
        warningView.setVisibility(VISIBLE);
        warningText.setText(msg);
    }

    public void removeError() {
        warningView.setVisibility(INVISIBLE);
    }

    public void setInputListener(OnPinCodeListener listener) {
        this.listener = listener;
    }

    private void initXmlStyle(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PinCodeLayout);
            pinCodeLeftRight = a.getDimensionPixelSize(R.styleable.PinCodeLayout_pinCodeMarginLeftRight, pinCodeLeftRight);
            pinCodeMarginBottom = a.getDimensionPixelSize(R.styleable.PinCodeLayout_pinCodeMarginBottom, pinCodeMarginBottom);
            maxCount = a.getInt(R.styleable.PinCodeLayout_pinCodeMaxCount, maxCount);
        }
    }

    private void init(final Context context) {
        this.context = context;
        setWillNotDraw(false);
        setOrientation(LinearLayout.VERTICAL);

        pinCodeLayout = new LinearLayout(context);
//        pinCodeLayout.setId(R.id.pin_header);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        p.bottomMargin = pinCodeMarginBottom;
        p.leftMargin = pinCodeLeftRight;
        p.rightMargin = pinCodeLeftRight;
        pinCodeLayout.setLayoutParams(p);
        pinCodeLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 1; i <= maxCount; i++) {
            editTextList.add(getPinEditText(context));
            setEditTextListeners();
        }
        setEditTexts(editTextList);

        RelativeLayout.LayoutParams giParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

//        giParam.addRule(RelativeLayout.BELOW, R.id.pin_header);

        addView(pinCodeLayout);

        KeyboardManager.setFocusAndOpenKeyboard(getContext(), editTextList.get(0));
//        addView(getWarningView());
//        addView(guardaInputLayout);

//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clearAll();
//                KeyboardManager.setFocusAndOpenKeyboard(getContext(), editTextList.get(0));
//            }
//        });
    }

    private void setEditTextListeners() {
        for (final EditText et : editTextList) {
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d("!!!!!", "beforeTextChanged " + charSequence.toString());
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    int currentPosition = getCurrentEtPosition(et);
                    if (charSequence.toString().length() == 0) {
                        editTextList.contains(et);
                        Log.d("!!!!!", "editTextList.contains(et) " + editTextList.contains(et) + " pos = " + currentPosition);
                        if (currentPosition != 0) {
                            KeyboardManager.setFocusAndOpenKeyboard(getContext(), editTextList.get(currentPosition - 1));
                        }
                    } else if (charSequence.toString().length() == 1) {
                        if (currentPosition < editTextList.size() - 1) {
                            KeyboardManager.setFocusAndOpenKeyboard(getContext(), editTextList.get(currentPosition + 1));
                        } else if (currentPosition == editTextList.size() - 1) {
                            pinText = getPinCodeText();
                            Toast.makeText(getContext(), pinText, Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearAll();
                    KeyboardManager.setFocusAndOpenKeyboard(getContext(), editTextList.get(0));
                }
            });

            et.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        //this is for backspace
                        int currentPosition = getCurrentEtPosition(et);
                        if (TextUtils.isEmpty(et.getText().toString())) {
                            editTextList.get(currentPosition - 1).setText("");
                            KeyboardManager.setFocusAndOpenKeyboard(getContext(), editTextList.get(currentPosition - 1));
                        }
                    }
                    return false;
                }
            });
        }

    }

    private String getPinCodeText() {
        StringBuilder res = new StringBuilder();
        for (EditText et : editTextList) {
            res.append(et.getText().toString());
        }
        return res.toString();
    }

    private int getCurrentEtPosition(EditText et) {
        for (int i = 0; i < editTextList.size(); i++) {
            if (editTextList.get(i).equals(et)) {
                return i;
            }
        }
        return -1;
    }

    private void clearAll() {
        for (int i = 0; i < editTextList.size(); i++) {
            editTextList.get(i).setText("");
        }
    }

    private void setEditTexts(List<EditText> editTextList) {
        for (EditText et : editTextList) {
            pinCodeLayout.addView(et);
        }
    }

//    private View getWarningView() {
//        warningView = LayoutInflater.from(context).inflate(R.layout.pin_code_warning_layout, null);
//        warningText = (TextView) warningView.findViewById(R.id.tv_warning_text);
//        warningView.setVisibility(INVISIBLE);
//        return warningView;
//    }

//    private void checkPinCodeImg(int colCheckedPin) {
//        for (int i = 0; i < maxCount; i++) {
//            ImageView pinImageView = (ImageView) pinCodeLayout.getChildAt(i);
//            if (i < colCheckedPin) {
//                pinImageView.setImageResource(R.drawable.ic_pin_code_fill);
//            } else {
//                pinImageView.setImageResource(R.drawable.ic_pin_code);
//            }
//        }
//        invalidate();
//    }

//    private View getPinImageView(Context context) {
//        ImageView imageView = new ImageView(context);
//        imageView.setImageResource(R.drawable.ic_pin_code);
//
//        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        p.weight = 1;
//        imageView.setLayoutParams(p);
//
//        return imageView;
//    }

    private EditText getPinEditText(Context context) {
        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(1);
        editText.setFilters(FilterArray);

        editText.setBackground(context.getResources().getDrawable(R.drawable.edit_text_border));
        editText.setCursorVisible(false);
        editText.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        editText.setTextSize(16);
        editText.setPadding(0, 0, 0, 0);
//        editText.setEnabled(false);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(70, 100);
        p.leftMargin = 25;
        p.rightMargin = 25;
        editText.setLayoutParams(p);

        return editText;
    }

    public void callError() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VIBRATE_ERROR_TIME);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                guardaInputLayout.clearText();
            }
        }, VIBRATE_ERROR_TIME);

    }

}
