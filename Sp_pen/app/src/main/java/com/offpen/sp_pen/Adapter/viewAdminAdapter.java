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
import com.offpen.sp_pen.model.viewadminModel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class viewAdminAdapter extends RecyclerView.Adapter<viewAdminAdapter.adminViewHolder> {
    ArrayList<viewadminModel> dataholderadmin;


    public viewAdminAdapter(ArrayList<viewadminModel> dataholderadmin) {
        this.dataholderadmin = dataholderadmin;
    }

    @NonNull
    @Override
    public viewAdminAdapter.adminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewAdminAdapter.adminViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewadmin_listitem, null));

    }

    @Override
    public void onBindViewHolder(@NonNull viewAdminAdapter.adminViewHolder holder, int position) {
        holder.name.setText(dataholderadmin.get(position).getF_name());
        holder.user_type.setText(dataholderadmin.get(position).getUser_type());

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
        return dataholderadmin.size();
    }


    class adminViewHolder extends RecyclerView.ViewHolder {
        TextView name, img, user_type;
        CheckBox checkBox;

        public adminViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.adminname);
            checkBox = itemView.findViewById(R.id.checkBox);
            user_type = itemView.findViewById(R.id.user_type);
        }
    }

    public static class DeleteAdmin extends AsyncTask<String, Void, String> {
    String userid;

        public DeleteAdmin(String userid) {
            this.userid = userid;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                String sql = "delete from user_master where userid = '" + userid + "'";
                st.executeUpdate(sql);
                System.out.println("deleted succesfully");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class ArchiveAdmin extends AsyncTask<String, Void, String> {

        String userid;

        public ArchiveAdmin(String userid) {
            this.userid = userid;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();
                System.out.println(userid + "  User Id");
                String sql = "update user_master set ArStatus = '" + 1 + "' where userid =  '" + userid + "'";
                st.executeUpdate(sql);
                System.out.println("archieved succesfully");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
