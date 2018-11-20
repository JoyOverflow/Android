package pxgd.hyena.com.buddylist;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String[] list={"徐浩然","王启帆","田展旭","何欣朋","唐艺芯","赵悦"};
        ArrayAdapter<String> ada=new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_activated_1,
                list);
        setListAdapter(ada);

    }
}
