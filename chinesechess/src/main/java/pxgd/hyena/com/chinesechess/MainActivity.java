package pxgd.hyena.com.chinesechess;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String DOMOB_PUBLISHER_ID_STRING = "56OJyOeouMzH2P6sIM";
    ChessboardView mChessboardView;
    RelativeLayout mMainLayout;
    LinearLayout mMenuLayout;
    Button mNewGameButton;
    Button mContinueButton;
    TextView mInfoTextView;
    boolean mIsUIStart = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = (RelativeLayout)findViewById(R.id.mainview);
        mMenuLayout = (LinearLayout)findViewById(R.id.menu_view);
        mNewGameButton = (Button)findViewById(R.id.new_game_btn);
        mContinueButton = (Button)findViewById(R.id.restore_game_btn);
        mChessboardView = (ChessboardView)findViewById(R.id.chessboard);
        mInfoTextView = (TextView)findViewById(R.id.info_tv);
        mChessboardView.setInfoTextview(mInfoTextView);

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mChessboardView.newGame();
                switchViewTo(mMainLayout);
                mIsUIStart = false;
            }
        });
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mIsUIStart) {
                    mChessboardView.restoreGameStatus();
                }
                switchViewTo(mMainLayout);
                mIsUIStart = false;
            }
        });

        /*
        // domob ad
        LinearLayout layout = (LinearLayout)findViewById(R.id.AdLinearLayout);
        DomobAdView adView = new DomobAdView(this,DOMOB_PUBLISHER_ID_STRING,DomobAdView.INLINE_SIZE_320X50);
        layout.addView(adView);
        DomobUpdater.checkUpdate(this, DOMOB_PUBLISHER_ID_STRING);
        */
    }

    void switchViewTo(View v) {
        if (v == mMainLayout) {
            mMenuLayout.setVisibility(View.INVISIBLE);
            mMainLayout.setVisibility(View.VISIBLE);
        }else if (v == mMenuLayout) {
            mMenuLayout.setVisibility(View.VISIBLE);
            mMainLayout.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        if (mMainLayout.getVisibility() == View.VISIBLE) {
            switchViewTo(mMenuLayout);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
        mChessboardView.saveGameStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.level1_menu:
                mChessboardView.setAILevel(3);
                mChessboardView.newGame();
                break;
            case R.id.level2_menu:
                mChessboardView.setAILevel(4);
                mChessboardView.newGame();
                break;
            case R.id.level3_menu:
                mChessboardView.setAILevel(5);
                mChessboardView.newGame();
                break;
            case R.id.about_menu:
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle(R.string.about_title);
                dialog.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }






}
