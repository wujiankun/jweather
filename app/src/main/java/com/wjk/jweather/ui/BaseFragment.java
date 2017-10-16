package com.wjk.jweather.ui;

import android.content.Context;
import android.support.v4.app.Fragment;


public abstract class BaseFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChooseAreaFragment.OnFragmentInteractionListener) {
            mListener = (ChooseAreaFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mListener.onFragmentSelect(this);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentSelect(BaseFragment fragment);
    }

    public abstract boolean handleBackPress();
}
