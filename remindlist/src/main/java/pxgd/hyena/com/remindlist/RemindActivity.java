package pxgd.hyena.com.remindlist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class RemindActivity extends AppCompatActivity {

    private ListView mListView;
    private RemindDb mDbAdapter;
    private RemindCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        //在标题栏显示自定义图标
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        //查找列表视并取消列表视的分隔线（使布局文件中的斜面分隔效果能正确显示）
        mListView = (ListView) findViewById(R.id.r_list_view);
        mListView.setDivider(null);


        //打开数据库
        mDbAdapter = new RemindDb(this);
        mDbAdapter.open();

        //清空表并加入一批备注记录（活动第一次创建时）
        if (savedInstanceState == null) {
            mDbAdapter.deleteAllReminders();
            insertReminders();
        }


        //读取数据绑定到列表视
        Cursor cursor = mDbAdapter.fetchAllReminders();
        String[] from = new String[]{RemindDb.COL_CONTENT};
        int[] to = new int[]{R.id.row_text};
        mCursorAdapter = new RemindCursorAdapter(
                RemindActivity.this,//上下文
                R.layout.list_row,//项布局
                cursor,//游标
                from,//数据库列
                to, //布局内元素
                0 //未使用参数
        );
        mListView.setAdapter(mCursorAdapter);





    }
    private void insertReminders() {
        mDbAdapter.createReminder("Buy Learn Android Studio", true);
        mDbAdapter.createReminder("Send Dad birthday gift", false);
        mDbAdapter.createReminder("Dinner at the Gage on Friday", false);
        mDbAdapter.createReminder("String squash racket", false);
        mDbAdapter.createReminder("Shovel and salt walkways", false);
        mDbAdapter.createReminder("Prepare Advanced Android syllabus", true);
        mDbAdapter.createReminder("Buy new office chair", false);
        mDbAdapter.createReminder("Call Auto-body shop for quote", false);
        mDbAdapter.createReminder("Renew membership to club", false);
        mDbAdapter.createReminder("Buy new Galaxy Android phone", true);
        mDbAdapter.createReminder("Sell old Android phone - auction", false);
        mDbAdapter.createReminder("Buy new paddles for kayaks", false);
        mDbAdapter.createReminder("Call accountant about tax returns", false);
        mDbAdapter.createReminder("Buy 300,000 shares of Google", false);
        mDbAdapter.createReminder("Call the Dalai Lama back", true);
    }
}
