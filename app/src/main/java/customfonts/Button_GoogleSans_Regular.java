package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class Button_GoogleSans_Regular extends androidx.appcompat.widget.AppCompatButton {

    public Button_GoogleSans_Regular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Button_GoogleSans_Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Button_GoogleSans_Regular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Regular.ttf");
            setTypeface(tf);
        }
    }

}