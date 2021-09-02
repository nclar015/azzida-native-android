package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EditText_GoogleSans_Italic extends androidx.appcompat.widget.AppCompatEditText {

    public EditText_GoogleSans_Italic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditText_GoogleSans_Italic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_GoogleSans_Italic(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Italic.ttf");
            setTypeface(tf);
        }
    }

}