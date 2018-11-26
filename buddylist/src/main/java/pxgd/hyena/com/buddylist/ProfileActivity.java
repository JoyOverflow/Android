package pxgd.hyena.com.buddylist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView name;
    private TextView location;
    private TextView age;
    private TextView onlineStatus;
    private EditText description;
    private ImageView profileImage;

    public static final String Image="image";
    public static final String Name="name";
    public static final String Age="age";
    public static final String Location="location";
    public static final String Descr="descr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        name = (TextView) findViewById(R.id.name);
        location = (TextView) findViewById(R.id.location);
        age = (TextView) findViewById(R.id.age);
        description = (EditText) findViewById(R.id.description);
        profileImage = (ImageView) findViewById(R.id.profile_image);


        int imageId = getIntent().getIntExtra(Image, -1);
        profileImage.setImageDrawable(getResources().getDrawable(imageId));
        name.setText(getIntent().getStringExtra(Name));
        location.setText(getIntent().getStringExtra(Location));
        age.setText(getIntent().getStringExtra(Age));
        description.setText(getIntent().getStringExtra(Descr));


        View parent = (View) name.getParent();
        parent.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        name.setTextAppearance(this,android.R.style.TextAppearance_DeviceDefault_Large);
        location.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
        location.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Inverse);
        age.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Inverse);
        description.setEnabled(false);
        description.setBackgroundColor(getResources().getColor(android.R.color.white));
        description.setTextColor(getResources().getColor(android.R.color.black));
    }
}
