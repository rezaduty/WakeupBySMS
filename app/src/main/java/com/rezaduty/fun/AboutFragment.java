package com.rezaduty.fun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午3:28
 * Mail: specialcyci@gmail.com
 */
public class AboutFragment extends Fragment {
    public View v;
    private static String PACKAGE_NAME = "com.rezaduty.fun";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about, container, false);

        ImageView img_telegram = (ImageView) view.findViewById(R.id.img_contact_telegram);
        img_telegram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=rezaduty"));
                startActivity(intent);
            }
        });

        ImageView img_email = (ImageView) view.findViewById(R.id.img_contact_email);
        img_email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "reza.duty.persian@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "عنوان");
                intent.putExtra(Intent.EXTRA_TEXT, "متن");
                startActivity(Intent.createChooser(intent, ""));
            }
        });









        return  view;

    }



}
