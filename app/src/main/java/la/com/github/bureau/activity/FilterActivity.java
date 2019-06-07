package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import la.com.github.bureau.R;
import la.com.github.bureau.fragment.FilterFragment;

public class FilterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        setActivityCode(0);
        return new FilterFragment();
    }

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, FilterActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.translate, R.anim.translate);
    }
}
