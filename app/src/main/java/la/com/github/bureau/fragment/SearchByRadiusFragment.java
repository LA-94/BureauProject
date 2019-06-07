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
import la.com.github.bureau.activity.SearchByRadiusActivity;

public class SearchByRadiusFragment extends Fragment {


    private ImageButton btnCheck;


    private RadioGroup mRadiusRadioGroup;
    private RadioButton RadioButtonIsChecked;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_search_by_radius, container, false);
        init(view);
        addActionListener();
        setRadioButtonIsChecked();
        return view;
    }

    private void addActionListener() {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = mRadiusRadioGroup.getCheckedRadioButtonId();
                RadioButtonIsChecked = getActivity().findViewById(radioButtonId);
                int radiusIndexRadioGroup = mRadiusRadioGroup.indexOfChild(RadioButtonIsChecked);
                FilterFragment.mCreateFilter.setRadiusDistanceIndex(radiusIndexRadioGroup);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.translate,R.anim. translate);
            }
        });
    }

    private void init(View view) {
        btnCheck = view.findViewById(R.id.check_btn);
        mRadiusRadioGroup = view.findViewById(R.id.radius_radio_group);
    }

    private void setRadioButtonIsChecked() {
        RadioButtonIsChecked = (RadioButton) mRadiusRadioGroup.getChildAt(FilterFragment.mCreateFilter.getRadiusDistanceIndex());
        RadioButtonIsChecked.setChecked(true);
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SearchByRadiusActivity.class);
        return intent;
    }
}
