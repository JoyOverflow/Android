package pxgd.hyena.com.remindlist;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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




        //读取数据并创建适配器
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
        //适配器绑定到列表视
        mListView.setAdapter(mCursorAdapter);

        //当前设备的SDK版本
        final int versionSdk=Build.VERSION.SDK_INT;

        //列表视的单击事件处理（弹出一个带列表视的对话框AlertDialog），传入一个实现了指定接口的匿名类
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                Toast.makeText(
                        RemindActivity.this,
                        "sdk version："+versionSdk+" - clicked："+position,
                        Toast.LENGTH_SHORT
                ).show();

                //显示对话框(v7)（调用AlertDialog类的内嵌静态类Builder）
                AlertDialog.Builder builder = new AlertDialog.Builder(RemindActivity.this);
                ListView modeListView = new ListView(RemindActivity.this);

                //注：第0项是"编辑"，第1项是"删除"
                String[] list = {"编辑备注", "删除备注"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(
                        RemindActivity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        list
                );
                //列表视绑定适配器，将列表视设置给对话框
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);

                //显示对话框
                final Dialog dialog = builder.create();
                dialog.show();


                //点击对话框内的列表视，又会弹出一个带布局的自定义对话框
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view, int mposition, long id) {

                        if (mposition == 0) {
                            //弹出新建或编辑对话框
                            int nId = getIdFromPosition(position);
                            Remind reminder = mDbAdapter.fetchReminderById(nId);
                            fireCustomDialog(reminder);
                        }
                        else{
                            //删除备注
                            mDbAdapter.deleteReminderById(getIdFromPosition(position));
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                        }
                        //退出（彻底解除）对话框
                        dialog.dismiss();
                    }
                });


                //以下代码仅在API11或更高版本上运行
                if (versionSdk >= Build.VERSION_CODES.HONEYCOMB) {
                    //设置成多选模式
                    mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

                    //单击列表项不会触发此事件（长按菜会触发），选中项的背景在布局文件v11内设置
                    //多选模式（可选择多行，重复点击则取消选中）
                    mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        //当长按列表视的表项时会调用onCreateActionMode方法
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            //填充并显示一个上下文菜单（仅出现在活动的标题栏）
                            MenuInflater inflater = mode.getMenuInflater();
                            inflater.inflate(R.menu.cam_menu, menu);
                            return true;
                        }
                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }
                        //运行菜单项
                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {
                                //上下文菜单的菜单项
                                case R.id.item_delete_reminder:
                                    for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                        if (mListView.isItemChecked(nC)) {
                                            mDbAdapter.deleteReminderById(getIdFromPosition(nC));
                                        }
                                    }
                                    //禁用多选操作模式，回到列表视的正常状态
                                    mode.finish();

                                    //将新的游标传递给适配器对象
                                    Cursor newCur=mDbAdapter.fetchAllReminders();
                                    mCursorAdapter.changeCursor(newCur);
                                    return true;
                            }
                            //返回false（表示其他事件监听可以处理此事件）
                            return false;
                        }
                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                        }
                        @Override
                        public void onItemCheckedStateChanged(
                                ActionMode mode,
                                int position,
                                long id,
                                boolean checked) {
                        }
                    });
                }

            }
        });



    }
    private int getIdFromPosition(int nC) {
        return (int) mCursorAdapter.getItemId(nC);
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

    /**
     * 修增两用的对话框
     * @param reminder
     */
    private void fireCustomDialog(final Remind reminder) {
        //定制一个无标题栏的对话框
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //对话框的布局
        dialog.setContentView(R.layout.dialog_custom);
        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);

        //判断是否是新添加或修改
        final boolean isEditOperation = (reminder != null);
        if (isEditOperation) {
            //修改为编辑备注
            titleView.setText("Edit Reminder");
            checkBox.setChecked(reminder.getImportant() == 1);
            editCustom.setText(reminder.getContent());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        //提交按钮
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remindTxt = editCustom.getText().toString();
                if (isEditOperation) {
                    //编辑备注信息
                    Remind remindEdit = new Remind(
                            reminder.getId(),
                            remindTxt,
                            checkBox.isChecked() ? 1 : 0
                    );
                    mDbAdapter.updateReminder(remindEdit);
                } else {
                    //创建新备注
                    mDbAdapter.createReminder(remindTxt, checkBox.isChecked());
                }
                //更新适配器游标，关闭对话框
                Cursor newCur=mDbAdapter.fetchAllReminders();
                mCursorAdapter.changeCursor(newCur);
                dialog.dismiss();
            }
        });
        //取消按钮（关闭对话框）
        Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //活动类加载菜单（使标题栏出现溢出菜单按钮）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mains, menu);
        return true;
    }





}
