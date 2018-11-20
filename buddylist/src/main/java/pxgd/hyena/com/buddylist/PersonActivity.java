package pxgd.hyena.com.buddylist;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class PersonActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Person p=(Person)l.getItemAtPosition(position);
        Intent i=new Intent(this,ProfileActivity.class);
        i.putExtra(ProfileActivity.Image,p.image);
        i.putExtra(ProfileActivity.Name,p.name);
        i.putExtra(ProfileActivity.Age,p.age);
        i.putExtra(ProfileActivity.Location,p.location);
        i.putExtra(ProfileActivity.Descr,p.descr);
        startActivity(i);
    }
}
