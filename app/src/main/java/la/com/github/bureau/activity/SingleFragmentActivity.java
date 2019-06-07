package la.com.github.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

import la.com.github.bureau.R;
import la.com.github.bureau.ac.DisconnectionActivity;
import la.com.github.bureau.pojo.User;


public abstract class SingleFragmentActivity extends AppCompatActivity {


    private static int mActivityCode;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private static FirebaseStorage sFirebaseStorage;
    private static StorageReference sAdStorageReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "user";
    private User mUserApp;

    protected abstract Fragment createFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.translate, R.anim.translate);
        hasConnection(this);
        setContentView(
                R.layout.ac_fragment);
        mAuth = FirebaseAuth.getInstance();
        sFirebaseStorage = FirebaseStorage.getInstance();
        sAdStorageReference = sFirebaseStorage.getReference().child("ad_photos");
        mUserApp = User.getInstance();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_adds_ad_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_adds_ad_container, fragment)
                    .commit();
        }
        changePressedBtn();

        if (!hasConnection(this)) {
            startActivity(DisconnectionActivity.getIntent(SingleFragmentActivity.this));
        }
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
                if (mUser != null) {
                    onSignedInit();
                }
                if (mActivityCode != AdListActivity.ACTIVITY_CODE) {
                    if (mUser == null) {
                        onSingedOutCleanUp();
                        List<AuthUI.IdpConfig> providers = Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build());

                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(providers)
                                        .build(),
                                RC_SIGN_IN);
                    } else {
                        onSignedInit();
                    }
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(mUser.getUid());


                Toast.makeText(SingleFragmentActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                startActivity(AdListActivity.getIntent(SingleFragmentActivity.this));
                finish();
            }
        }
    }

    public void onSearchBtnClick(View view) {
        startActivity(AdListActivity.getIntent(this));
    }

    public void onMyAdsBtnClick(View view) {
        startActivity(MyAdsActivity.getIntent(this));
    }

    public void onAddAdBtnClick(View view) {
        startActivity(AddsAdActivity.getIntent(this));
    }

    public void onMessageBtnClick(View view) {

    }

    public void onProfileBtnClick(View view) {
        startActivity(ProfileUserActivity.getIntent(this));
    }


    private void changePressedBtn() {
        LinearLayout mMenuLinearLayout = findViewById(R.id.main_menu_linear_layout);
        ((ImageButton) mMenuLinearLayout.getChildAt(mActivityCode))
                .setColorFilter(getResources()
                        .getColor(R.color.colorItemText));
    }


    protected void setActivityCode(int activityCode) {
        mActivityCode = activityCode;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void onSignedInit() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users");
        Query getUser = reference.whereEqualTo("uid", mUser.getUid());

        getUser.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                                mUserApp = task.getResult().getDocuments().get(0).toObject(User.class);
                            } else {
                                addNewUser();
                            }
                        }
                    }
                });
    }

    private void addNewUser() {
        mUserApp.setName(ANONYMOUS);
        mUserApp.setPhoneNumber(mUser.getPhoneNumber());
        mUserApp.setUID(mUser.getUid());
        FirebaseFirestore.getInstance().collection("users")
                .add(mUserApp)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                    }
                });
    }


    private void onSingedOutCleanUp() {

    }

    public static StorageReference getAdStorageReference() {
        return sAdStorageReference;
    }

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
