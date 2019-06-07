package la.com.github.bureau.ac;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import la.com.github.bureau.activity.SingleFragmentActivity;
import la.com.github.bureau.fragment.ProfileFragment;

public class ProfileActivity extends SingleFragmentActivity {
    public static final int ACTIVITY_CODE = 4;

    @Override
    protected Fragment createFragment() {
        setActivityCode(ACTIVITY_CODE);
        return new ProfileFragment();
    }

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ProfileActivity.class);
        return intent;
    }

    @Override
    public void onProfileBtnClick(View view) {

    }
}
