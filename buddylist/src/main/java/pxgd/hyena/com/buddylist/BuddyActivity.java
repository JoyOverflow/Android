package pxgd.hyena.com.buddylist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * （3）使用片段来适配屏幕-ListView列表视
 */
public class BuddyActivity extends FragmentActivity
        implements BuddyListFragment.ListSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy);

        //是否存在片段容器
        if(findViewById(R.id.fragment_container)!=null) {

            //活动第一次创建时 savedInstanceState=null
            //活动已创建，设备发生旋转 savedInstanceState！=null
            //活动失去焦点后又回到活动中（或活动从后台又回到前台）onCreate不会被运行
            if (savedInstanceState != null) {
                return;
            }
            //创建列表片断对象
            BuddyListFragment buddyList = new BuddyListFragment();

            //将所有意图信息作为参数传递给它（无意义，可删除！！！）
            //buddyList.setArguments(getIntent().getExtras());

            //事务加载列表片段
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, buddyList);
            transaction.commit();
        }
    }


    /**
     * 活动类实现的接口方法将由列表片断类来调用（传入当前被单击的Person类对象）
     * @param selectedPerson
     */
    @Override
    public void onListItemSelected(Person selectedPerson) {

        //从详情片段控件内查找详情片段类实例
        //普通布局（非Large布局）中无此控件因此总是为null，每次均需新创建详情片段类实例
        BuddyDetailFragment buddyDetail = (BuddyDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if (buddyDetail != null) {
            buddyDetail.updateDetailView(selectedPerson);
        } else {

            //新创建详情片段类实例
            buddyDetail = new BuddyDetailFragment();

            //创建意图（包含当前单击对象信息）
            Bundle args = new Bundle();
            args.putInt(BuddyDetailFragment.Image, selectedPerson.image);
            args.putString(BuddyDetailFragment.Name, selectedPerson.name);
            args.putString(BuddyDetailFragment.Location, selectedPerson.location);
            args.putInt(BuddyDetailFragment.Age, selectedPerson.age);
            args.putString(BuddyDetailFragment.Descr, selectedPerson.descr);

            //传递意图给详情片段对象
            buddyDetail.setArguments(args);

            //片段事务中替换掉（更新）当前片段，并将该事务添加到回退栈
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, buddyDetail);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
