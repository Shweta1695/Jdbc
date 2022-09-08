package com.offpen.sp_pen.Adapter;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.offpen.sp_pen.R;
import com.offpen.sp_pen.model.viewstudentmodel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class viewstudentAdapter extends RecyclerView.Adapter<viewstudentAdapter.studentViewHolder> {
    ArrayList<viewstudentmodel> dataholderstudent;

    public viewstudentAdapter(ArrayList<viewstudentmodel> data) {
        this.dataholderstudent = data;
    }


    @NonNull
    @Override
    public viewstudentAdapter.studentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new studentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewstudent_listitem, null));
    }

    @Override
    public void onBindViewHolder(@NonNull viewstudentAdapter.studentViewHolder holder, int position) {

        holder.name.setText(dataholderstudent.get(position).getF_name());
        holder.stdId.setText(Integer.toString(dataholderstudent.get(position).getId()));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    Toast.makeText(v.getContext(), "You selected " + holder.name.getText(), Toast.LENGTH_SHORT)
                            .show();

                } else if (!v.isSaveEnabled()) {
                    Toast.makeText(v.getContext(), "You NOT selected " + holder.name.getText(), Toast.LENGTH_SHORT)
                            .show();

                }
                System.out.println(holder.name.getText());
                Toast.makeText(v.getContext(), "You selected " + holder.name.getText(), Toast.LENGTH_SHORT)
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataholderstudent.size();

    }


    class studentViewHolder extends RecyclerView.ViewHolder {
        TextView name, stdId;
        CheckBox checkBox;

        public studentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentname);
            stdId = itemView.findViewById(R.id.studentId);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }


    public static class DeleteStudent extends AsyncTask<String, Void, String> {
        int stid;

        public DeleteStudent(int stid) {
            this.stid = stid;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                String sql = "delete from studentmaster where studentid = '" + stid + "'";
                st.executeUpdate(sql);
                System.out.println("deleted succesfully");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class ArchiveStudent extends AsyncTask<String, Void, String> {

        int studentid;

        public ArchiveStudent(int studentid) {
            this.studentid = studentid;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();
                System.out.println(studentid + "  Student Id");
                String sql = "update studentmaster set ArStatus = '" + 1 + "' where studentId =  '" + studentid + "'";
                st.executeUpdate(sql);
                System.out.println("archieved succesfully");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}