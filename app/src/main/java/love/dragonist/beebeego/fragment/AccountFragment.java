package love.dragonist.beebeego.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import love.dragonist.beebeego.R;


public class AccountFragment extends Fragment {
    private TextView textExit;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initData();

        textExit = view.findViewById(R.id.account_text_exit);

        initListener();
        return view;
    }

    private void initData() {

    }

    private void initListener() {
        textExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);
                TextView cancel = view.findViewById(R.id.dialog_confirm_text_cancel);
                cancel.setText("取消操作");
                TextView confirm = view.findViewById(R.id.dialog_confirm_text_confirm);
                confirm.setText("退出帐号");
                ((ImageView) view.findViewById(R.id.dialog_confirm_img)).setImageResource(R.drawable.ic_bee_unchecked);
                ((TextView) view.findViewById(R.id.dialog_confirm_text_title)).setText("退出提示");
                ((TextView) view.findViewById(R.id.dialog_confirm_text_hint)).setText("您是否退出当前帐号？");
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("fragment", "account");
                            jsonObject.put("action", "exit");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mListener.onFragmentInteraction(jsonObject);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(JSONObject jsonObject);
    }
}
