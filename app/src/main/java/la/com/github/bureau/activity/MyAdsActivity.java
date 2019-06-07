package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import la.com.github.bureau.fragment.MyAdsFragment;

public class MyAdsActivity extends SingleFragmentActivity {

    public static final int ACTIVITY_CODE = 1;

    @Override
    protected Fragment createFragment() {
        setActivityCode(ACTIVITY_CODE);
        return new MyAdsFragment();
    }

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MyAdsActivity.class);
        return intent;
    }

    @Override
    public void onMyAdsBtnClick(View view) {

    }

    @Override
    public void onBackPressed() {
        startActivity(AdListActivity.getIntent(this));
    }
}
