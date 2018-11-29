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

    //定义接口
    public interface ListSelectedListener {
        void onListItemSelected(Person selectedPerson);
    }
    //接口成员字段
    private ListSelectedListener listSelectedListener;




    public BuddyListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示列表
        Person[] items = new Person[]{
                new Person(R.drawable.mark,"徐浩然",42,"New York",""),
                new Person(R.drawable.joseph,"王启帆",32,"Virginia",""),
                new Person(R.drawable.mark, "田展旭",28,"Carolina",""),
                new Person(R.drawable.leah,"唐艺芯",36,"Denver","")
        };
        setListAdapter(new PersonAdapter(getActivity(),
                android.R.layout.simple_expandable_list_item_2,
                items)
        );
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_buddy_list, container, false);
    }


    /**
     * 获取关联的活动类实例（将其转换为接口字段成员）
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    //列表视的项点击事件
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //调用接口成员字段的接口方法（传入当前单击对象）
        Person person = (Person) l.getItemAtPosition(position);
        this.listSelectedListener.onListItemSelected(person);
    }


}
