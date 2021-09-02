package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EditText_GoogleSans_Bold extends androidx.appcompat.widget.AppCompatEditText {

    public EditText_GoogleSans_Bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditText_GoogleSans_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_GoogleSans_Bold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Bold.ttf");
            setTypeface(tf);
        }
    }

}