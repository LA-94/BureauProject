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

public class SearchByPublishedFragment extends Fragment {


    private ImageButton btnCheck;

    private RadioGroup mPublishedRadioGroup;
    private RadioButton mRadioButtonIsChecked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fr_search_by_published, container, false);
        init(view);
        addOnClickListener();
        setRadioButtonIsChecked();
        return view;
    }




    private void addOnClickListener() {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = mPublishedRadioGroup.getCheckedRadioButtonId();
                mRadioButtonIsChecked = getActivity().findViewById(radioButtonId);
                int radiusIndexRadioGroup = mPublishedRadioGroup.indexOfChild(mRadioButtonIsChecked);
                FilterFragment.mCreateFilter.setPublishedIndex(radiusIndexRadioGroup);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.translate,R.anim. translate);
            }
        });
    }


    private void setRadioButtonIsChecked(){
        mRadioButtonIsChecked = (RadioButton) mPublishedRadioGroup.getChildAt(FilterFragment.mCreateFilter.getPublishedIndex());
        mRadioButtonIsChecked.setChecked(true);
    }

    private void init(View view) {
        btnCheck = view.findViewById(R.id.check_btn);

        mPublishedRadioGroup = view.findViewById(R.id.published_radio_group);

    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, AdListFragment.class);
        return intent;
    }
}
