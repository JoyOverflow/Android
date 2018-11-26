package pxgd.hyena.com.testmodule;

import android.app.ListActivity;
import android.os.Bundle;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取自己的手机屏幕密度（dpi值）
        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;


        Person[] list={
                new Person(R.drawable.mary,"徐浩然",42,"",""),
                new Person(R.drawable.mary,"王启帆",32,"",""),
                new Person(R.drawable.mary,"田展旭",28,"",""),
                new Person(R.drawable.mary,"何欣朋",35,"",""),
                new Person(R.drawable.mary,"唐艺芯",36,"",""),
                new Person(R.drawable.mary,"赵悦",29,"","")

        };

        PersonAdapter ada=new PersonAdapter(
                this, android.R.layout.simple_list_item_activated_2, list
        );
        setListAdapter(ada);
    }
}
