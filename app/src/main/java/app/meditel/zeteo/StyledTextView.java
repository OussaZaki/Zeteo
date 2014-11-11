package app.meditel.zeteo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by FOla Yinka on 1/6/14.
 */
public class StyledTextView extends TextView {

    public StyledTextView(Context context) {
        super(context);
        style(context);


    }

    public StyledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public StyledTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);

    }


    private void style(Context context) {
        setTypeface(Constants.tf);
    }

}