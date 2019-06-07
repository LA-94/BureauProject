package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import la.com.github.bureau.fragment.ProfileUserFragment;

public class ProfileUserActivity extends SingleFragmentActivity {
    public static final int ACTIVITY_CODE = 4;

    @Override
    protected Fragment createFragment() {
        setActivityCode(ACTIVITY_CODE);
        return new ProfileUserFragment();
    }
    public static Intent getIntent(Context packageContext) {
        return new Intent(packageContext, ProfileUserActivity.class);
    }

    @Override
    public void onBackPressed() {
        startActivity(AdListActivity.getIntent(this));
    }
}
