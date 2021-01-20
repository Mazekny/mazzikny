package com.example.mazzikny;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestsAdapter extends ArrayAdapter<ProfileCard> {
    private Context context;
    int flag;
    public RequestsAdapter(Context context, ArrayList<ProfileCard> profileList){
        super(context, 0, profileList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ProfileCard profileItem = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_card_layout, parent, false);

        TextView userName = (TextView)convertView.findViewById(R.id.member_name);
        TextView userInst = (TextView) convertView.findViewById(R.id.member_inst);
        Button button = (Button) convertView.findViewById(R.id.req_button);
        ImageView image = (ImageView) convertView.findViewById(R.id.image_view);

        userName.setText(profileItem.getUserName());
        userInst.setText(profileItem.getUserInstrument());
        flag = profileItem.getSentFlag();

        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("dp1", "drawable", context.getPackageName());
        image.setImageResource(resourceId);

        if (flag == 1) {
            button.setText("Pending");
        }
        else {
            button.setText("Accept");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setText("Accepted");
                    button.setClickable(false);
                    sendRequest(profileItem.getId());
                }
            });
        }
        return convertView;
    }

    public void sendRequest(String userID) {
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        final String checkAppIDURL = "http://10.0.2.2:3000";
//        user.getIdToken(true).addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.e(TAG, "Could not get authentication token.");
//                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Log.d("Connection", "Got token");
//            String idToken = task.getResult().getToken();
        StringRequest getUserDetails = new StringRequest(Request.Method.POST,
                checkAppIDURL + "/acceptConnection",
                response -> {
                    System.out.println(response);
//                        try {
//                            getUserDetailsJSON(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                }, error -> {
            System.out.println(error);
        }
        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Token ");
////                    params.put("pic", imageToString(bitmap));
//                return params;
//            }
            @Override
            public byte[] getBody()
            // To set the POST request body, we need to override the "getBody" function. String.getBytes() usually works here.
            // You should use it in conjunction with the Google Gson library to easily convert classes to JSON.
            {
                String body = "uid=" + userID;
                return body.getBytes();
            }
        };
        queue.add(getUserDetails);
    }
}
