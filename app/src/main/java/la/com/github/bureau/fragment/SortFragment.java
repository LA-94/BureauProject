package la.com.github.bureau.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import la.com.github.bureau.R;
import la.com.github.bureau.activity.SortActivity;

public class SortFragment extends Fragment {


    private ImageButton btnCheck;

    private RadioButton mRadioButtonIsChecked;
    private RadioGroup mSortRadioGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_sort, container, false);
        init(view);
        addOnClickListener();
        setRadioButtonIsChecked();
        return view;
    }

    private void addOnClickListener() {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = mSortRadioGroup.getCheckedRadioButtonId();
                mRadioButtonIsChecked = getActivity().findViewById(radioButtonId);
                int radiusIndexRadioGroup = mSortRadioGroup.indexOfChild(mRadioButtonIsChecked);
                FilterFragment.mCreateFilter.setSortByIndex(radiusIndexRadioGroup);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.translate,R.anim. translate);
            }
        });
    }


    private void setRadioButtonIsChecked() {
        mRadioButtonIsChecked = (RadioButton) mSortRadioGroup.getChildAt(FilterFragment.mCreateFilter.getSortByIndex());
        mRadioButtonIsChecked.setChecked(true);
    }

    private void init(View view) {
        btnCheck = view.findViewById(R.id.check_btn);
        mSortRadioGroup = view.findViewById(R.id.sort_by_radio_group);
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SortActivity.class);
        return intent;
    }
}
