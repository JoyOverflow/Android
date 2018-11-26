package pxgd.hyena.com.testmodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PersonAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private Person[] items;

    public PersonAdapter(Context context, int layout, Person[] items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.length;
    }
    @Override
    public Object getItem(int position) {
        return items[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //判断项视图是否为空
        View view = convertView;
        if (view==null) {
            //调用系统服务来绘制项视图
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);
        }

        //查找视图元素，设置视图元素文本值
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        TextView text2 = (TextView) view.findViewById(android.R.id.text2);
        text1.setText(items[position].name);
        text2.setText(String.valueOf(items[position].age));
        return view;
    }

}
