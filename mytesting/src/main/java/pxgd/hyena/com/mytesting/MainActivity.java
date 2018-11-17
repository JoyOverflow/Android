package pxgd.hyena.com.mytesting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //隐藏当前活动的标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }
}
