package com.rezaduty.mmmm.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rezaduty.mmmm.R;

public class PersonViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView deletePerson;
    public  ImageView editPerson;

    public PersonViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.person_name);
        deletePerson = (ImageView)itemView.findViewById(R.id.delete_person);
        editPerson = (ImageView)itemView.findViewById(R.id.edit_person);
    }
}
