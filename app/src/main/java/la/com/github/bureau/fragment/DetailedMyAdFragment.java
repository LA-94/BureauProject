package la.com.github.bureau.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.UUID;

import la.com.github.bureau.R;
import la.com.github.bureau.activity.SingleFragmentActivity;
import la.com.github.bureau.pojo.Ad;
import la.com.github.bureau.pojo.BulletinBoard;
import la.com.github.bureau.pojo.CategoryBase;

public class DetailedMyAdFragment extends Fragment {


    private static final String ARG_AD_ID = "la.com.github.bureau.arg_ad_id";


    private static final String DIALOG_DATE = "la.com.github.bureau.fr_dialog_date";
    private static final String DIALOG_TITLE = "la.com.github.bureau.fr_dialog_title";
    private static final String DIALOG_DESCRIPTION = "la.com.github.bureau.fr_dialog_description";
    private static final String DIALOG_CATEGORY = "la.com.github.bureau.fr_dialog_category";

    private static final int REQUEST_CODE_DATE = 0;
    private static final int REQUEST_CODE_CATEGORY = 1;
    public static final int REQUEST_CODE_TITLE = 2;
    public static final int REQUEST_CODE_DESCRIPTION = 3;
    private static final int RC_PHOTO_PICKER = 4;
    public static final int REQUEST_CODE_REASON = 5;

    private LinearLayout mAdFoundLinearLayout;
    private LinearLayout mAdLocationLinearLayout;
    private LinearLayout mAdCategoryLinearLayout;
    private ProgressBar mProgressBar;
    private LinearLayout mAdDescriptionLinearLayout;
    private TextView mAdTitleTextView;
    private TextView mIsReturnTextView;
    private TextView mAdFoundDateTextView;
    private TextView mLocationTextView;
    private TextView mDescriptionTextView;
    private TextView mCategoryTextView;
    private ImageView mFindThingImageView;
    private Button mSaveChangesButton;
    private Button mRemovePublishedAdButton;

    private Uri mSelectedImageUri;
    private Ad mAd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getContext(), "Нажмите на элемент, чтобы внести редактирование",
                Toast.LENGTH_SHORT).show();
        UUID adId = (UUID) getArguments().getSerializable(ARG_AD_ID);
        mAd = BulletinBoard.get(getActivity()).getAd(adId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_detailed_my_ad, container, false);
        init(view);
        return view;
    }


    private void init(View v) {

        boolean isReturnAd = mAd.isReturn();
        mIsReturnTextView = v.findViewById(R.id.is_returned_text_view);
        mIsReturnTextView.setVisibility((isReturnAd ? View.VISIBLE : View.GONE));
        mAdTitleTextView = v.findViewById(R.id.ad_title_text_view);
        mAdTitleTextView.setText(mAd.getTitle());
        mAdTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DialogEditTextAdFragment dialog = DialogEditTextAdFragment.getInstance(mAd.getTitle());
                dialog.setTargetFragment(DetailedMyAdFragment.this, REQUEST_CODE_TITLE);
                dialog.show(manager, DIALOG_TITLE);
            }
        });

        mAdFoundDateTextView = v.findViewById(R.id.ad_found_date_text_view);
        mAdFoundDateTextView.setText(Ad.dateFormatted(mAd.getFoundDate()));
        mAdFoundLinearLayout = v.findViewById(R.id.ad_found_linear_layout);
        mAdFoundLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.getInstance(new Date(mAd.getFoundDate()));
                dialog.setTargetFragment(DetailedMyAdFragment.this, REQUEST_CODE_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mProgressBar = v.findViewById(R.id.progress_bar);

        mLocationTextView = v.findViewById(R.id.location_text_view);

        mDescriptionTextView = v.findViewById(R.id.description_text_view);
        mDescriptionTextView.setText(mAd.getDetails());

        mCategoryTextView = v.findViewById(R.id.category_text_view);
        mCategoryTextView.setText(getString(CategoryBase.getInstance()
                .getCategoryTexResourceById(mAd.getCategory())));

        mFindThingImageView = v.findViewById(R.id.find_thing_image_view);
        Glide.with(mFindThingImageView.getContext())
                .load(mAd.getPhotoUrl())
                .into(mFindThingImageView);
        mFindThingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        mSaveChangesButton = v.findViewById(R.id.save_changes_button);
        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                setEnabledViews(false);

                if (mSelectedImageUri != null) {
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference deleteFile = firebaseStorage.getReferenceFromUrl(mAd.getPhotoUrl());
                    int count = 0;
                    for (int i = 0; i < BulletinBoard.get(getContext()).getAds().size(); i++) {

                        if (deleteFile.getDownloadUrl().equals(BulletinBoard.get(getContext()).getAds().get(i))) {
                            count++;
                        }
                    }
                    if (count == 1) {
                        deleteFile.delete();
                    }

                    final StorageReference photoRef = SingleFragmentActivity.getAdStorageReference().child(mSelectedImageUri.getLastPathSegment());
                    photoRef.putFile(mSelectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            } else {
                            }
                            return photoRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                mAd.setPhotoUrl(downloadUrl.toString());
                                setAd();
                            }
                        }

                    });

                } else {
                    setAd();
                }
            }
        });

        mRemovePublishedAdButton = v.findViewById(R.id.remove_published_ad_button);
        mRemovePublishedAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DialogRemoveByPublish dialog = new DialogRemoveByPublish();
                dialog.setTargetFragment(DetailedMyAdFragment.this, REQUEST_CODE_REASON);
                dialog.show(manager, DIALOG_CATEGORY);
            }
        });


        mAdLocationLinearLayout = v.findViewById(R.id.ad_location_linear_layout);
        mAdLocationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(MapActivity.getIntent(getActivity()));
            }
        });

        mAdCategoryLinearLayout = v.findViewById(R.id.ad_category_linear_layout);
        mAdCategoryLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DialogCategoryFragment dialog = new DialogCategoryFragment();
                dialog.setTargetFragment(DetailedMyAdFragment.this, REQUEST_CODE_CATEGORY);
                dialog.show(manager, DIALOG_CATEGORY);
            }
        });

        mAdDescriptionLinearLayout = v.findViewById(R.id.ad_description_linear_layout);
        mAdDescriptionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DialogEditTextAdFragment dialog = DialogEditTextAdFragment.getInstance(mAd.getDetails());
                dialog.setTargetFragment(DetailedMyAdFragment.this, REQUEST_CODE_DESCRIPTION);
                dialog.show(manager, DIALOG_DESCRIPTION);
            }
        });
    }


    private void setAd() {
        mAd.setPlacementDate(new Date().getTime());
        CollectionReference reference = FirebaseFirestore.getInstance().collection("ads");
        reference.document("" + mAd.getAdID())
                .set(mAd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        Toast.makeText(getContext(), "Объявление сохранено", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(getContext(), "Произошла ошибка, попробуйте еще раз)", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static DetailedMyAdFragment newInstance(UUID adId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_AD_ID, adId);
        DetailedMyAdFragment adFragment = new DetailedMyAdFragment();
        adFragment.setArguments(args);
        return adFragment;
    }


    private void setEnabledViews(boolean turnEnabled) {
        mSaveChangesButton.setEnabled(turnEnabled);
        mDescriptionTextView.setEnabled(turnEnabled);
        mCategoryTextView.setEnabled(turnEnabled);
        mAdFoundDateTextView.setEnabled(turnEnabled);
        mAdTitleTextView.setEnabled(turnEnabled);
        mFindThingImageView.setEnabled(turnEnabled);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mAd.setFoundDate(date.getTime());
            mAdFoundDateTextView.setText(Ad.dateFormatted(mAd.getFoundDate()));
        } else if (requestCode == REQUEST_CODE_CATEGORY) {
            int idCategory = data.getIntExtra(DialogCategoryFragment.EXTRA_ID_CATEGORY, 0);
            mAd.setCategory(idCategory);
            mCategoryTextView.setText(getString(CategoryBase.getInstance().getCategoryTexResourceById(idCategory)));
        } else if (requestCode == REQUEST_CODE_TITLE) {
            String title = data.getStringExtra(DialogEditTextAdFragment.EXTRA_TITLE);
            mAd.setTitle(title);
            mAdTitleTextView.setText(title);
        } else if (requestCode == REQUEST_CODE_DESCRIPTION) {
            String description = data.getStringExtra(DialogEditTextAdFragment.EXTRA_DESCRIPTION);
            mAd.setDetails(description);
            mDescriptionTextView.setText(description);
        } else if (requestCode == RC_PHOTO_PICKER) {
            mSelectedImageUri = data.getData();
            mFindThingImageView.setImageURI(mSelectedImageUri);
        } else if (requestCode == REQUEST_CODE_REASON) {
            int reason = data.getIntExtra(DialogRemoveByPublish.EXTRA_REASON, 0);
            if (reason == DialogRemoveByPublish.RETURNED_REASON_CODE) {
                mAd.setReturn(true);
                setAd();
            } else if (reason == DialogRemoveByPublish.ANOTHER_REASON_CODE) {
                CollectionReference reference = FirebaseFirestore.getInstance().collection("ads");
                reference.document(mAd.getAdID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgressBar.setVisibility(ProgressBar.GONE);
                                Toast.makeText(getContext(), "Объявление удалено", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                        Toast.makeText(getContext(), "Произошла ошибка, попробуйте еще раз)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}
