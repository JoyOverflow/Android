package pxgd.hyena.com.remindlist;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;

public class RemindCursorAdapter extends SimpleCursorAdapter {

    /**
     * 构造函数
     * @param context
     * @param layout
     * @param c
     * @param from
     * @param to
     * @param flags
     */
    public RemindCursorAdapter(
            Context context,
            int layout,
            Cursor c,
            String[] from,
            int[] to,
            int flags)
    {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        //查找到对应的ViewHolder
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            //创建一个ViewHolder
            holder = new ViewHolder();
            holder.colImp = cursor.getColumnIndexOrThrow(RemindDb.COL_IMPORTANT);
            holder.listTab = view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }if (cursor.getInt(holder.colImp) > 0) {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }
        else {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
    }
    /**
     * 内部静态类
     */
    static class ViewHolder {
        int colImp; //颜色标记的字段值
        View listTab;//颜色标记控件
    }
}
