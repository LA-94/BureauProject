package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import la.com.github.bureau.R;
import la.com.github.bureau.fragment.DetailedAdFragment;
import la.com.github.bureau.pojo.Ad;
import la.com.github.bureau.pojo.BulletinBoard;

public class AdPagerActivity extends AppCompatActivity {

    private static final String EXTRA_AD_ID = "la.com.github.bureau.extra_ad_id";

    private ViewPager mViewPager;
    private List<Ad> mAdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ad_pager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        UUID adId = (UUID) getIntent().getSerializableExtra(EXTRA_AD_ID);
        init();
        for(int i = 0; i< mAdList.size(); i++){
            if(mAdList.get(i).getId().equals(adId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }



    private void init() {


        mViewPager = findViewById(R.id.ad_view_pager);

        mAdList = BulletinBoard.get(this).getAds();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Ad mAd = mAdList.get(i);
                return DetailedAdFragment.newInstance(mAd.getId());
            }

            @Override
            public int getCount() {
                return mAdList.size();
            }
        });
    }

    public static Intent getIntent(Context packageContext, UUID adId) {
        Intent intent = new Intent(packageContext, AdPagerActivity.class);
        intent.putExtra(EXTRA_AD_ID, adId);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.translate, R.anim.translate);
    }
}
