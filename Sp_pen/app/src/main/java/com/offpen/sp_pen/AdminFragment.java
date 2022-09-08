package com.offpen.sp_pen;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.offpen.sp_pen.Adapter.viewAdminAdapter;
import com.offpen.sp_pen.model.viewadminModel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    public SharedPreferences preferences;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<viewadminModel> dataholderadmin;
    String orgId;
    String current_UserId;


    public AdminFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_admin);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        orgId = preferences.getString("orgId", "");
        current_UserId = preferences.getString("currentUserId", "");
        recyclerView = view.findViewById(R.id.recviewAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholderadmin = new ArrayList<>();
        viewadminsql viewadminobj = new viewadminsql();
        viewadminobj.execute();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Toast.makeText(getContext(), "Archived swiped left " + dataholderadmin.get(position).getF_name(), Toast.LENGTH_SHORT).show();
                    viewAdminAdapter.ArchiveAdmin archiveAdmin = new viewAdminAdapter.ArchiveAdmin(dataholderadmin.get(position).getUser_id());
                    archiveAdmin.execute();
                    Snackbar.make(recyclerView, dataholderadmin.get(position).getF_name() + " Archived", Snackbar.LENGTH_SHORT).show();
                    dataholderadmin.clear();
                    viewadminsql ob = new viewadminsql();
                    ob.execute();
                    synchronized (archiveAdmin) {
                        archiveAdmin.notify();
                    }
                    break;

                case ItemTouchHelper.RIGHT:
                    Toast.makeText(getContext(), " delete swiped right" + dataholderadmin.get(position).getF_name(), Toast.LENGTH_SHORT).show();
                    viewAdminAdapter.DeleteAdmin deleteAdmin = new viewAdminAdapter.DeleteAdmin(dataholderadmin.get(position).getUser_id());
                    deleteAdmin.execute();

                    Snackbar.make(recyclerView, dataholderadmin.get(position).getF_name(), Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO: write undo delete code here
                                }
                            }).show();

                    dataholderadmin.clear();
                    viewadminsql v = new viewadminsql();
                    v.execute();
                    synchronized (deleteAdmin) {
                        deleteAdmin.notify();
                    }
                    break;
            }
        }
    };


    public class viewadminsql extends AsyncTask<String, Void, String> {
        List<String> arrlist_f_name = new ArrayList<>();
        List<String> arrlist_type = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();

                System.out.println("ORG ID CHECK THIS.." + orgId + " Current user " + current_UserId);

                ResultSet rs = st.executeQuery("select fname,usertype,userid from user_master where arstatus = '" + 0 + "'and orgid = '" + orgId + "'and not userid = '" + current_UserId + "'");
                while (rs.next()) {
                    String admin_fname = rs.getString(1).trim();
                    arrlist_f_name.add(admin_fname);

                    String user_type = rs.getString(2).trim();
                    arrlist_type.add(user_type);

                    String admin_user_id = rs.getString(3).trim();
                    viewadminModel obj = new viewadminModel(admin_fname, user_type, admin_user_id);
                    obj.setF_name(admin_fname);
                    obj.setUser_type(user_type);
                    obj.setUser_id(admin_user_id);
                    dataholderadmin.add(obj);


                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new viewAdminAdapter(dataholderadmin));
                    }
                });
                con.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    dataholderadmin.clear();

                    viewadminsql ob = new viewadminsql();
                    ob.execute();

                    Toast.makeText(getContext(), "Reloaded ", Toast.LENGTH_SHORT)
                            .show();

                    // swipeRefreshLayout.setRefreshing(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Stop animation (This will be after 3 seconds)
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 4000);
                }
            });
            //Color Animation on swipe refresh
            swipeRefreshLayout.setColorSchemeColors(
                    getResources().getColor(android.R.color.holo_green_light),
                    getResources().getColor(android.R.color.holo_blue_bright),
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light)
            );
        }
    }

}













