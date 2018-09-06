package com.rezaduty.mmmm.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.rezaduty.mmmm.Person;
import com.rezaduty.mmmm.R;
import com.rezaduty.mmmm.database.SqliteDatabase;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonViewHolder>{

    private Context context;
    private List<Person> listPeople;

    private SqliteDatabase mDatabase;

    public PersonAdapter(Context context, List<Person> listPeople) {
        this.context = context;
        this.listPeople = listPeople;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_layout, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        final Person singlePerson = listPeople.get(position);

        holder.name.setText(singlePerson.getName());

        holder.editPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(singlePerson);
            }
        });

        holder.deletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database

                mDatabase.deleteProduct(singlePerson.getId());

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPeople.size();
    }


    private void editTaskDialog(final Person person){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_person_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText titleField = (EditText)subView.findViewById(R.id.enter_title);

        if(person != null){
            nameField.setText(person.getName());
            titleField.setText(String.valueOf(person.getTitle()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ویرایش");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ویرایش", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String title = String.valueOf(titleField.getText().toString());

                if(TextUtils.isEmpty(name) || title.length() <= 0){
                    Toast.makeText(context, "لطفا مقادیر خود را بررسی کنید", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updatePerson(new Person(person.getId(), name, title));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });

        builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "لغو شد.", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}
