package com.zcorp.app.tweety.Adapters;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Mankirat on 16-04-2017.
 */

public abstract class MyClickableSpan extends ClickableSpan {

    public abstract void onLongClick(View widget);
}
