package la.com.github.bureau.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

import la.com.github.bureau.R;
import la.com.github.bureau.activity.AdListActivity;

public class ProfileUserFragment extends Fragment {
    private Button mSignOutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_user_fragment, container, false);
        mSignOutBtn = v.findViewById(R.id.sign_out_btn);
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    AuthUI.getInstance().signOut(getActivity());
                    startActivity(AdListActivity.getIntent(getActivity()));
                    getActivity().finish();
                }
            }
        });
        return v;
    }
}
