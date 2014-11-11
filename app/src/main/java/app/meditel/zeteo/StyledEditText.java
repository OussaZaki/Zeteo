package app.meditel.zeteo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by FOla Yinka on 1/6/14.
 */
public class StyledEditText extends EditText {


    public StyledEditText(Context context) {
        super(context);
        style(context);

    }

    public StyledEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public StyledEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    private void style(Context context) {
        setTypeface(Constants.tf);
        setTextColor(Color.parseColor("#333333"));
    }

}