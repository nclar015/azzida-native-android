package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class MyTextView_GoogleSans_MediumItalic extends androidx.appcompat.widget.AppCompatTextView {

    public MyTextView_GoogleSans_MediumItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView_GoogleSans_MediumItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView_GoogleSans_MediumItalic(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-MediumItalic.ttf");
            setTypeface(tf);
        }
    }

}