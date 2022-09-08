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
import com.offpen.sp_pen.Adapter.viewstudentAdapter;
import com.offpen.sp_pen.model.viewstudentmodel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StudentFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    String orgId;
    public SharedPreferences preferences;
    ArrayList<viewstudentmodel> dataholderstudent;


    public StudentFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        recyclerView = view.findViewById(R.id.recviewStudent);
        swipeRefreshLayout = view.findViewById(R.id.swipe_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholderstudent = new ArrayList<>();

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        orgId = preferences.getString("orgId", "");

        StudentFragment.viewstudentsql viewstudentsqlobj = new StudentFragment.viewstudentsql();
        viewstudentsqlobj.execute();
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

                    Toast.makeText(getContext(), "swiped left" + dataholderstudent.get(position).getF_name(), Toast.LENGTH_SHORT).show();
                    System.out.println("ORG ID CHECK THIS.." + orgId);
                    viewstudentAdapter.ArchiveStudent archiveStudent = new viewstudentAdapter.ArchiveStudent(dataholderstudent.get(position).getId());
                    archiveStudent.execute();

                    Snackbar.make(recyclerView, dataholderstudent.get(position).getF_name() + " Archived", Snackbar.LENGTH_SHORT).show();

                    dataholderstudent.clear();
                    viewstudentsql vis = new viewstudentsql();
                    vis.execute();
                    synchronized (archiveStudent) {
                        archiveStudent.notify();
                    }
                    break;

                case ItemTouchHelper.RIGHT:

                    Toast.makeText(getContext(), " delete swiped right" + dataholderstudent.get(position).getF_name(), Toast.LENGTH_SHORT).show();
                    viewstudentAdapter.DeleteStudent deleteStudent = new viewstudentAdapter.DeleteStudent(dataholderstudent.get(position).getId());
                    deleteStudent.execute();

                    Snackbar.make(recyclerView, dataholderstudent.get(position).getF_name(), Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO: write undo delete code here
                                }
                            }).show();

                    dataholderstudent.clear();
                    viewstudentsql v = new viewstudentsql();
                    v.execute();
                    synchronized (deleteStudent) {
                        deleteStudent.notify();
                    }
                    break;
            }
        }
    };

    public class viewstudentsql extends AsyncTask<String, Void, String> {
        List<String> arrlist_f_name = new ArrayList<>();
        List<Integer> arrlist_id = new ArrayList<>();
        String res = "";


        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select fname, studentid from studentmaster where arstatus = '" + 0 + "'and orgid ='"+ orgId +"'");
                while (rs.next()) {

                    String student_fname = rs.getString(1).trim();
                    arrlist_f_name.add(student_fname);

                    int student_id = rs.getInt(2);
                    arrlist_id.add(student_id);

                    viewstudentmodel obj = new viewstudentmodel(student_fname, student_id);
                    obj.setF_name(student_fname);
                    obj.setId(student_id);
                    dataholderstudent.add(obj);

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new viewstudentAdapter(dataholderstudent));
                    }
                });
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return res;
            //TODO write code here to display student in recycler view , refer penfragment viewpensql
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    dataholderstudent.clear();

                    viewstudentsql ob = new viewstudentsql();
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
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light),
                    getResources().getColor(android.R.color.holo_blue_bright)
            );
        }
    }
}