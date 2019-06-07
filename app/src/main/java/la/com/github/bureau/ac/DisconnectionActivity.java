package la.com.github.bureau.ac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import la.com.github.bureau.R;
import la.com.github.bureau.activity.SingleFragmentActivity;

public class DisconnectionActivity extends AppCompatActivity {

    private Button btnUpdate;
    public static final String FROM_ACTIVITY = "la.com.github.bureau.from_activity";


    @Override
    protected void onCreate(Bundle savedInstanseState) {
        super.onCreate(savedInstanseState);

        setContentView(R.layout.ac_disconnection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        addOnClickListener();
    }

    private void init() {
        btnUpdate = findViewById(R.id.update_btn);
    }

    private void addOnClickListener() {
        final int activityCode = getIntent().getIntExtra(FROM_ACTIVITY, 0);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingleFragmentActivity.hasConnection(DisconnectionActivity.this)) {
                    finish();
                    overridePendingTransition(R.anim.translate, R.anim.translate);
                } else {
                    Toast.makeText(DisconnectionActivity.this, "Извините, но соеденение по прежнему прервано", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, DisconnectionActivity.class);
        return intent;
    }
}
