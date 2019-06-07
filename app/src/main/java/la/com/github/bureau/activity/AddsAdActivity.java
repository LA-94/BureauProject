package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import la.com.github.bureau.R;
import la.com.github.bureau.fragment.AddsAdFragment;

public class AddsAdActivity extends SingleFragmentActivity {
    public static final int ACTIVITY_CODE = 2;

    @Override
    protected Fragment createFragment() {
        setActivityCode(ACTIVITY_CODE);
        return new AddsAdFragment();
    }

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddsAdActivity.class);
        return intent;
    }

    @Override
    public void onAddAdBtnClick(View view) {

    }

    @Override
    public void onBackPressed() {
        startActivity(AdListActivity.getIntent(this));
    }
}
