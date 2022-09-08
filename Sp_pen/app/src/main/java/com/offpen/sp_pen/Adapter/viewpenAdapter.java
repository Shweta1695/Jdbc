package com.offpen.sp_pen.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.offpen.sp_pen.R;
import com.offpen.sp_pen.model.tagpenModel;
import com.offpen.sp_pen.model.viewpenmodel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class viewpenAdapter extends RecyclerView.Adapter<viewpenAdapter.myviewholder> {
    ArrayList<viewpenmodel> dataholder;
    Context context;

    public static ArrayList<tagpenModel> imageModelArrayList;


    public viewpenAdapter(ArrayList<viewpenmodel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpen_listitem, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        holder.name.setText(dataholder.get(position).getName());
        holder.type.setText(dataholder.get(position).getType());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected())
                {
                    Toast.makeText(v.getContext(), "You selected " + holder.name.getText(), Toast.LENGTH_SHORT)
                            .show();

                }
                else if(!v.isSaveEnabled())
                {
                    Toast.makeText(v.getContext(), "You NOT selected " + holder.name.getText(), Toast.LENGTH_SHORT)
                            .show();

                }
                System.out.println(holder.name.getText());
                Toast.makeText(v.getContext(), "You selected " + holder.name.getText(), Toast.LENGTH_SHORT)
                        .show();
                //String name =;
                //imageModelArrayList.add(dataholder.get(position).getName());

               // imageModelArrayList.add( holder.name.getText());

                //For pen deletion
                //DeletePen deletePen = new DeletePen(v);
                //deletePen.execute();

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {

        TextView name, type;
        CheckBox checkBox;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.penname);
            type = itemView.findViewById(R.id.pentype);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }

    public static class DeletePen extends AsyncTask<String, Void, String> {
        int sno;
        View view;

        public DeletePen(int name) {
            sno = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* Toast.makeText(view.getContext(), "On pre exe delete pen", Toast.LENGTH_SHORT)
                    .show();*/
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                String sql = "delete from pen_master where serialnumber = '" + sno + "'";
                st.executeUpdate(sql);
                System.out.println("deleted succesfully");

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public static class ArchivePen extends AsyncTask<String, Void, String> {

        String serialNumber;

        public ArchivePen(String sno) {
            serialNumber = sno;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();
                System.out.println(serialNumber + "  Serial number");
                String sql = "update pen_master set ArStatus = '" + 1 + "' where serialnumber =  '" + serialNumber + "'";
                st.executeUpdate(sql);
                System.out.println("archieved succesfully");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}