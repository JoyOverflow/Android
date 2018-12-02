package pxgd.hyena.com.buddylist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuddyListFragment extends ListFragment {

    //定义內嵌接口和接口成员字段
    public interface ListSelectedListener {
        void onListItemSelected(Person selectedPerson);
    }
    private ListSelectedListener listSelectedListener;


    public BuddyListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建对象数组
        Person[] items = new Person[]{
                new Person(R.drawable.mark,"徐浩然",42,"New York",""),
                new Person(R.drawable.joseph,"王启帆",32,"Virginia",""),
                new Person(R.drawable.mark, "田展旭",28,"Carolina",""),
                new Person(R.drawable.leah,"唐艺芯",36,"Denver","")
        };
        //创建自定义对象适配器
        PersonAdapter ada=new PersonAdapter(
                getActivity(),
                android.R.layout.simple_expandable_list_item_2,
                items
        );
        //调用基类方法，传入适配器对象
        setListAdapter(ada);
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        //加载片段的布局文件（内有系统约定id的列表视）
        return inflater.inflate(R.layout.fragment_buddy_list, container, false);
    }


    /**
     * 获取关联的活动类实例（将其转换为接口字段成员）
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //获取关联的活动类实例（活动已实现了此片段内嵌接口）
        this.listSelectedListener = (ListSelectedListener) context;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof ListSelectedListener)) {
            throw new ClassCastException("Activity should implement OnListItemSelectedListener");
        }
        this.listSelectedListener = (ListSelectedListener) activity;
    }
    //重写基类的列表视项点击事件
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //根据点击位置从列表视得到当前单击对象
        Person person = (Person) l.getItemAtPosition(position);
        //调用接口成员字段的方法（传入当前单击对象）（接口方法已由活动类实现）
        this.listSelectedListener.onListItemSelected(person);
    }


}
