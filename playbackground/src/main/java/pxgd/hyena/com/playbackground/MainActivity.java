package pxgd.hyena.com.playbackground;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnPlay = (Button)findViewById(R.id.btnPlay);
        final TextView textView = (TextView)findViewById(R.id.textView);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPlay.getText().equals("Start")) {
                    Intent myService = new Intent(MainActivity.this, PlaySoundService.class);
                    startService(myService);

                    btnPlay.setText("Pause");
                    textView.setTextColor(Color.parseColor("#FF0000"));

                } else {
                    Intent myService = new Intent(MainActivity.this, PlaySoundService.class);
                    stopService(myService);

                    btnPlay.setText("Start");
                    textView.setTextColor(Color.parseColor("#0000FF"));
                }
            }
        });





    }
}
