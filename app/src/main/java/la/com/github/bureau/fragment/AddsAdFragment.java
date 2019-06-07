package la.com.github.bureau.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import la.com.github.bureau.R;
import la.com.github.bureau.activity.AddsAdActivity;
import la.com.github.bureau.activity.SingleFragmentActivity;
import la.com.github.bureau.pojo.Ad;
import la.com.github.bureau.pojo.CategoryBase;

public class AddsAdFragment extends Fragment {

    private static final String DIALOG_DATE = "la.com.github.bureau.fr_dialog_date";
    private static final String DIALOG_TITLE = "la.com.github.bureau.fr_dialog_title";
    private static final String DIALOG_DESCRIPTION = "la.com.github.bureau.fr_dialog_description";
    private static final String DIALOG_CATEGORY = "la.com.github.bureau.fr_dialog_category";

    private static final int REQUEST_CODE_DATE = 0;
    private static final int REQUEST_CODE_CATEGORY = 1;
    public static final int REQUEST_CODE_TITLE = 2;
    public static final int REQUEST_CODE_DESCRIPTION = 3;
    private static final int RC_PHOTO_PICKER = 4;

    private Ad mAd;
    private LinearLayout mAdFoundLinearLayout;
    private LinearLayout mAdLocationLinearLayout;
    private LinearLayout mAdCategoryLinearLayout;
    private ProgressBar mProgressBar;
    private LinearLayout mAdDescriptionLinearLayout;
    private Button mPublishButton;

    private TextView mAdTitleTextView;
    private TextView mAdFoundDateTextView;
    private TextView mAdCategoryTextView;
    private TextView mAdDescriptionTextView;
    private ImageView mFindThingImageView;
    private Uri mSelectedImageUri;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_adds_ad, container, false); //inflate = заполнять
        init(v);
        return v;
    }

    private void init(View v) {
        mAd = new Ad();
        mAdFoundDateTextView = v.findViewById(R.id.ad_found_date_text_view);
        mProgressBar = v.findViewById(R.id.progress_bar);
        mAdDescriptionTextView = v.findViewById(R.id.ad_description_text_view);
        mAdCategoryTextView = v.findViewById(R.id.category_text_view);

        mFindThingImageView = v.findViewById(R.id.find_thing_image_view);
        mFindThingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        mAdTitleTextView = v.findViewById(R.id.ad_title_text_view);
        mAdTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DialogEditTextAdFragment dialog = DialogEditTextAdFragment.getInstance(mAd.getTitle());
                dialog.setTargetFragment(AddsAdFragment.this, REQUEST_CODE_TITLE);
                dialog.show(manager, DIALOG_TITLE);
            }
        });

        mAdFoundLinearLayout = v.findViewById(R.id.ad_found_linear_layout);
        mAdFoundLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.getInstance(new Date(mAd.getFoundDate()));
                dialog.setTargetFragment(AddsAdFragment.this, REQUEST_CODE_DATE);
                dialog.show(manager, DIALOG_DATE);
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
                dialog.setTargetFragment(AddsAdFragment.this, REQUEST_CODE_CATEGORY);
                dialog.show(manager, DIALOG_CATEGORY);
            }
        });

        mAdDescriptionLinearLayout = v.findViewById(R.id.ad_description_linear_layout);
        mAdDescriptionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DialogEditTextAdFragment dialog = DialogEditTextAdFragment.getInstance(mAd.getDetails());
                dialog.setTargetFragment(AddsAdFragment.this, REQUEST_CODE_DESCRIPTION);
                dialog.show(manager, DIALOG_DESCRIPTION);
            }
        });

        mPublishButton = v.findViewById(R.id.publish_button);
        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //добавление изображения и занесение в бд

                mAd.setUserId();

                if (mSelectedImageUri == null) {
                    Toast.makeText(getActivity(), "Выберите фотографию!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAdTitleTextView.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Заполните заголовок!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAdFoundDateTextView.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Заполните дату обнаружения!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAdCategoryTextView.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Заполните категорию!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAdDescriptionTextView.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Заполните описание!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                setEnabledViews(false);


                final StorageReference photoRef =
                        SingleFragmentActivity.getAdStorageReference().child(mSelectedImageUri.getLastPathSegment());
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
                            FirebaseFirestore.getInstance().collection("ads")
                                    .add(mAd)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), "Успешно добавленно", Toast.LENGTH_SHORT).show();
                                            mProgressBar.setVisibility(ProgressBar.GONE);
                                            startActivity(AddsAdActivity.getIntent(getActivity()));
                                            getActivity().finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(getActivity(), "Возникли проблемы. Пожалуйста повторите))", Toast.LENGTH_SHORT).show();
                                            setEnabledViews(true);

                                        }
                                    });
                        }
                    }
                });

            }
        });
    }

    private void setEnabledViews(boolean turnEnabled) {
        mPublishButton.setEnabled(turnEnabled);
        mAdDescriptionTextView.setEnabled(turnEnabled);
        mAdCategoryTextView.setEnabled(turnEnabled);
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
            mAdCategoryTextView.setText(getString(CategoryBase.getInstance().getCategoryTexResourceById(idCategory)));
        } else if (requestCode == REQUEST_CODE_TITLE) {
            String title = data.getStringExtra(DialogEditTextAdFragment.EXTRA_TITLE);
            mAd.setTitle(title);
            mAdTitleTextView.setText(title);
        } else if (requestCode == REQUEST_CODE_DESCRIPTION) {
            String description = data.getStringExtra(DialogEditTextAdFragment.EXTRA_DESCRIPTION);
            mAd.setDetails(description);
            mAdDescriptionTextView.setText(description);
        } else if (requestCode == RC_PHOTO_PICKER) {
            mSelectedImageUri = data.getData();
            mFindThingImageView.setImageURI(mSelectedImageUri);
        }
    }


}
