package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import la.com.github.bureau.R;
import la.com.github.bureau.activity.SingleFragmentActivity;
import la.com.github.bureau.fragment.SortFragment;

public class SortActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        setActivityCode(0);
        return new SortFragment();
    }

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SortActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.translate, R.anim.translate);
    }
}
