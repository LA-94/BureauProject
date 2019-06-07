package la.com.github.bureau.pojo;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BulletinBoard {

    public static BulletinBoard sBulletinBoard;

    private List<Ad> mAds;

    public static BulletinBoard get(Context context) {
        if (sBulletinBoard == null) {
            sBulletinBoard = new BulletinBoard(context);
        }
        return sBulletinBoard;
    }


    private BulletinBoard(Context context) {
        mAds = new ArrayList<>();
    }

    public List<Ad> getAds() {
        return mAds;
    }


    public void addAd(Ad newAd) {
        if (newAd == null) {
            mAds.clear();
            return;
        }
        mAds.add(newAd);
    }

    public void addAds(List<Ad> newAds) {
        mAds.clear();

        if (newAds.isEmpty()) {
            return;
        }
        mAds.addAll(newAds);
    }


    public Ad getAd(UUID id) {
        for (Ad ad : mAds) {
            if (ad.getId().equals(id)) {
                return ad;
            }
        }
        return null;
    }

    public void clearBulletinBoard() {
        mAds.clear();
    }
}
