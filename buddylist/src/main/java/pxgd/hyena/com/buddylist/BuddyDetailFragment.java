package pxgd.hyena.com.buddylist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuddyDetailFragment extends Fragment {

    public static final String Image="image";
    public static final String Name="name";
    public static final String Age="age";
    public static final String Location="location";
    public static final String Descr="descr";
    private Person person;


    public BuddyDetailFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        updatePersonDetail(savedInstanceState);
        return inflater.inflate(R.layout.fragment_buddy_detail, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        updatePersonDetail(getArguments());
    }

    public void updateDetailView(Person person) {
        FragmentActivity activity = getActivity();
        ImageView profileImage = (ImageView) activity.findViewById(R.id.profile_image);
        TextView name = (TextView) activity.findViewById(R.id.name);
        TextView location = (TextView) activity.findViewById(R.id.location);
        TextView age = (TextView) activity.findViewById(R.id.age);
        EditText description = (EditText) activity.findViewById(R.id.description);
        profileImage.setImageDrawable(getResources().getDrawable(person.image));
        name.setText(person.name);
        location.setText(person.location);
        age.setText(String.valueOf(person.age));
        description.setText(person.descr);
    }
    private void updatePersonDetail(Bundle bundle) {
        if (bundle != null) {
            this.person = new Person(
                    bundle.getInt(Image),
                    bundle.getString(Name),
                    bundle.getInt(Age),
                    bundle.getString(Location),
                    bundle.getString(Descr)
            );
        }
        //if we have a valid person from the bundle
        //or from restored state then update the screen
        if(this.person !=null){
            updateDetailView(this.person);
        }
    }
}
