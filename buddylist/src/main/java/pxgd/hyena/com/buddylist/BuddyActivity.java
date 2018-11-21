package pxgd.hyena.com.buddylist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class BuddyActivity extends FragmentActivity
        implements BuddyListFragment.OnListItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy);


        //是否存在片段容器
        if(findViewById(R.id.fragment_container)!=null) {

            if (savedInstanceState != null) {
                return;
            }
            //加载列表片段
            BuddyListFragment buddyListFragment = new BuddyListFragment();
            buddyListFragment.setArguments(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, buddyListFragment);
            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onListItemSelected(Person selectedPerson) {

        BuddyDetailFragment buddyDetailFragment =
                (BuddyDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if (buddyDetailFragment != null) {
            buddyDetailFragment.updateDetailView(selectedPerson);
        } else {

            buddyDetailFragment = new BuddyDetailFragment();

            Bundle args = new Bundle();
            args.putInt(BuddyDetailFragment.Image, selectedPerson.image);
            args.putString(BuddyDetailFragment.Name, selectedPerson.name);
            args.putString(BuddyDetailFragment.Location, selectedPerson.location);
            args.putInt(BuddyDetailFragment.Age, selectedPerson.age);
            args.putString(BuddyDetailFragment.Descr, selectedPerson.descr);
            buddyDetailFragment.setArguments(args);


            //
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, buddyDetailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

}
