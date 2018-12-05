package pxgd.hyena.com.testmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏活动的状态栏和标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        Button btnLevel=(Button)findViewById(R.id.gameLevel);
        Button btnRandom=(Button)findViewById(R.id.gameRandom);
        Button btnTimer=(Button)findViewById(R.id.gameTime);
        Button btnSuper=(Button)findViewById(R.id.gameSuper);
        btnLevel.setOnClickListener(listen);
        btnRandom.setOnClickListener(listen);
        btnTimer.setOnClickListener(listen);
        btnSuper.setOnClickListener(listen);
    }

    View.OnClickListener listen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String str="";
            switch (view.getId()) {
                case R.id.gameLevel:
                    str="GameLevel";
                    break;
                case R.id.gameRandom:
                    str="GameRandom";
                    break;
                case R.id.gameTime:
                    str="GameTime";
                    break;
                case R.id.gameSuper:
                    str="GameSuper";
                    break;
            }
            Toast.makeText(
                    MainActivity.this,
                    str,
                    Toast.LENGTH_SHORT
            ).show();
        }
    };




}
