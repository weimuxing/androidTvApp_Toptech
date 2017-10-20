package com.mstar.miscsetting.dialog;


import com.mstar.miscsetting.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Otherset extends Activity{

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    private TextView picture;
    private TextView sound;
    private TextView s3d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.othersetting);
        findView();
    }

    private void findView(){
        picture = (TextView)findViewById(R.id.textview_pic);
//        picture.requestFocus();
        sound = (TextView)findViewById(R.id.textview_audio);
        picture.requestFocus();
        s3d = (TextView)findViewById(R.id.textview_audio);
    }

}
