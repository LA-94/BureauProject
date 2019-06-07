package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import la.com.github.bureau.fragment.AdListFragment;

public class AdListActivity extends SingleFragmentActivity {
    public static final int ACTIVITY_CODE = 0;

    @Override
    protected Fragment createFragment() {
        setActivityCode(ACTIVITY_CODE);
        return new AdListFragment();
    }

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AdListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public void onSearchBtnClick(View view) {

    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Нажмите кнопку \"назад\" еще раз чтобы выйти",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

}
