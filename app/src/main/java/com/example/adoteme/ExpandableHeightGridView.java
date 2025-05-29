package com.example.adoteme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/** GridView que mede TODAS as linhas (bom para usar dentro de ScrollView). */
public class ExpandableHeightGridView extends GridView {

    public ExpandableHeightGridView(Context c) { super(c); }
    public ExpandableHeightGridView(Context c, AttributeSet a) { super(c, a); }
    public ExpandableHeightGridView(Context c, AttributeSet a, int s) { super(c, a, s); }

    @Override protected void onMeasure(int wSpec, int hSpec) {
        /* Mede a altura inteira: PASSO-MAIOR que o necessário */
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(wSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();   // grava a altura calculada
    }
}
