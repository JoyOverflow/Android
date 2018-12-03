package pxgd.hyena.com.remindlist;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //在标题栏显示自定义图标
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        //查找到列表视
        mListView = (ListView) findViewById(R.id.r_list_view);
        //取消列表视的分隔线（使布局文件中的斜面分隔效果能正确显示）
        mListView.setDivider(null);

        //数组适配器
        ArrayAdapter<String> array=new ArrayAdapter<>(
                this,
                R.layout.list_row,
                R.id.row_text,
                new String[]{
                        "first record",
                        "second record",
                        "third record",
                        "fourth record",
                        "fifth record",
                        "sixth record",
                        "seventh record",
                        "eighth record",
                        "ninth record",
                        "tenth record",
                        "分水岭，分水线，分水界"
                }
        );
        //为视图绑定适配器
        mListView.setAdapter(array);

        //列表视的单击事件处理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
