package la.com.github.bureau.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import la.com.github.bureau.R;
import la.com.github.bureau.pojo.Ad;
import la.com.github.bureau.pojo.BulletinBoard;
import la.com.github.bureau.pojo.CategoryBase;
import la.com.github.bureau.pojo.UserForAds;

public class DetailedAdFragment extends Fragment {

    private static final String ARG_AD_ID = "la.com.github.bureau.arg_ad_id";

    private Ad mAd;

    private TextView mAdTitleTextView;
    private TextView mIsReturnTextView;
    private TextView mAdFoundDateTextView;
    private TextView mAdPublishedDateTextView;
    private TextView mLocationTextView;
    private TextView mDiscriptionTextView;
    private TextView mCategoryTextView;
    private ImageView mFindThingImageView;
    private ImageButton mGoToPhoneImageButton;
    private ImageButton mGoToMessageImageButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID adId = (UUID) getArguments().getSerializable(ARG_AD_ID);
        mAd = BulletinBoard.get(getActivity()).getAd(adId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_detailed_ad, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        boolean isReturnAd = mAd.isReturn();
        mAdTitleTextView = v.findViewById(R.id.ad_title_text_view);
        mAdTitleTextView.setText(mAd.getTitle());
        mAdFoundDateTextView = v.findViewById(R.id.ad_found_date_text_view);
        mAdFoundDateTextView.setText(Ad.dateFormatted(mAd.getFoundDate()));
        mAdPublishedDateTextView = v.findViewById(R.id.ad_published_date_text_view);
        mAdPublishedDateTextView.setText(Ad.dateFormatted(mAd.getPlacementDate()));
        mLocationTextView = v.findViewById(R.id.location_text_view);
        mDiscriptionTextView = v.findViewById(R.id.description_text_view);
        mDiscriptionTextView.setText(mAd.getDetails());
        mCategoryTextView = v.findViewById(R.id.category_text_view);
        mFindThingImageView = v.findViewById(R.id.find_thing_image_view);
        Glide.with(mFindThingImageView.getContext())
                .load(mAd.getPhotoUrl())
                .into(mFindThingImageView);

        CollectionReference reference = FirebaseFirestore.getInstance().collection("users");
        Query getUser = reference.whereEqualTo("uid", mAd.getUserId());

        final UserForAds userForAds = new UserForAds();

        getUser.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                                userForAds.setAll(task.getResult().getDocuments().get(0).toObject(UserForAds.class));
                            }
                        }
                    }
                });
        mGoToPhoneImageButton = v.findViewById(R.id.save_changes_button);
        mGoToPhoneImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPhoneHardware()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userForAds.getPhoneNumber()));
                    if (getContext() != null && intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.telephone_number)
                            .setMessage(userForAds.getPhoneNumber())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }
                Toast.makeText(getActivity(), "I'm call by number" + userForAds.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            }
        });

        mGoToMessageImageButton = v.findViewById(R.id.remove_published_ad_button);
        mGoToMessageImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", userForAds.getPhoneNumber(), null)));
                Toast.makeText(getActivity(), "I'm message by number" + userForAds.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            }
        });

        mCategoryTextView = v.findViewById(R.id.category_text_view);
        mCategoryTextView.setText(getString(CategoryBase.getInstance()
                .getCategoryTexResourceById(mAd.getCategory())));

        if (isReturnAd) {
            mGoToMessageImageButton.setVisibility(View.GONE);
            mGoToPhoneImageButton.setVisibility(View.GONE);
        } else {
            mGoToMessageImageButton.setVisibility(View.VISIBLE);
            mGoToPhoneImageButton.setVisibility(View.VISIBLE);
        }

        mIsReturnTextView = v.findViewById(R.id.is_returned_text_view);
        mIsReturnTextView.setVisibility((isReturnAd ? View.VISIBLE : View.GONE));
    }

    public static DetailedAdFragment newInstance(UUID adId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_AD_ID, adId);

        DetailedAdFragment adFragment = new DetailedAdFragment();
        adFragment.setArguments(args);
        return adFragment;
    }

    private boolean hasPhoneHardware() {
        if (getActivity() == null) {
            return false;
        }
        PackageManager packageManager = getActivity().getPackageManager();

        boolean telephonySupported = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        boolean gsmSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_GSM);
        boolean cdmaSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA);

        return telephonySupported && gsmSupported && cdmaSupported;
    }
}
