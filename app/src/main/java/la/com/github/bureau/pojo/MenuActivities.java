package la.com.github.bureau.pojo;

import android.content.Context;
import android.content.Intent;

import la.com.github.bureau.ac.ProfileActivity;
import la.com.github.bureau.activity.AddsAdActivity;
import la.com.github.bureau.activity.AdListActivity;

public class MenuActivities {

    public static Intent getIntent(Context context, int activityCode) {
        Intent intent = null;
        switch (activityCode) {
            case AdListActivity
                    .ACTIVITY_CODE:
                intent = AdListActivity.getIntent(context);
                break;
            /*case AdListActivity
                    .ACTIVITY_CODE:
                return AdListActivity.class;*/
            case AddsAdActivity
                    .ACTIVITY_CODE:
                intent = AddsAdActivity.getIntent(context);
                break;
            /*case AdListActivity
                    .ACTIVITY_CODE:
                return AdListActivity.class;*/
            case ProfileActivity
                    .ACTIVITY_CODE:
                intent = ProfileActivity.getIntent(context);
                break;
        }
        return intent;
    }
}