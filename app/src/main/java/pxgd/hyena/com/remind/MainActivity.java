package pxgd.hyena.com.remind;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private RemindDb mDbAdapter;
    private RemindCursorAdapter mCursorAdapter;


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
        mListView = (ListView) findViewById(R.id.reminders_list_view);
        //取消列表视的分隔线（使布局文件中的斜面分隔效果能正确显示）
        mListView.setDivider(null);

        //数组适配器
        ArrayAdapter<String> array=new ArrayAdapter<>(this,
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
        //列表视的单击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }



    //加载菜单布局文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                //创建新备忘录（弹出定制的对话框）
                //fireCustomDialog(null);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }
    }






}
