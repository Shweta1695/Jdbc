package com.offpen.sp_pen;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.offpen.sp_pen.Adapter.viewpenAdapter;
import com.offpen.sp_pen.Adapter.viewstudentAdapter;
import com.offpen.sp_pen.model.viewpenmodel;
import com.offpen.sp_pen.utils.variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PenFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    ArrayList<viewpenmodel> dataholder;
    String mParam1;
    String mParam2;
    public  SharedPreferences preferences;
    String orgId;
    SwipeRefreshLayout swipeRefreshLayout;


    public PenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  new async1.execute();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pen, container, false);
        recyclerView = view.findViewById(R.id.recview);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholder = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        orgId = preferences.getString("orgId", "");

        System.out.println("pen fragment inside");
        viewpensql viewpensqlobj = new viewpensql();
        viewpensqlobj.execute();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }

    String deleteMovie = null;


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

                    Toast.makeText(getContext(), "swiped left" + dataholder.get(position).getName(), Toast.LENGTH_SHORT).show();

                    System.out.println("ORG ID CHECK THIS.." + orgId);
                    viewpenAdapter.ArchivePen archivePen = new viewpenAdapter.ArchivePen(dataholder.get(position).getName());
                    archivePen.execute();

                    Snackbar.make(recyclerView, dataholder.get(position).getName() + " Archived", Snackbar.LENGTH_SHORT).show();

                    dataholder.clear();
                    viewpensql vi = new viewpensql();
                    vi.execute();
                    synchronized (archivePen) {
                        archivePen.notify();
                    }
                    break;

                case ItemTouchHelper.RIGHT:
                    deleteMovie = dataholder.get(position).getName();

                    Toast.makeText(getContext(), "swiped right" + dataholder.get(position).getName(), Toast.LENGTH_SHORT).show();
                    viewpenAdapter.DeletePen deletePen = new viewpenAdapter.DeletePen(Integer.parseInt(dataholder.get(position).getName()));
                    deletePen.execute();


                    Snackbar.make(recyclerView, dataholder.get(position).getName(), Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO: write undo delete code here
                                }
                            }).show();

                    dataholder.clear();
                    viewpensql v = new viewpensql();
                    v.execute();
                    synchronized (deletePen) {
                        deletePen.notify();
                    }
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            //external library
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.green1))
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.red1))
                    .addSwipeRightActionIcon(R.drawable.delete)
                    .addSwipeRightLabel(getString(R.string.delete))
                    .setSwipeRightLabelColor(Color.WHITE)
                    .addSwipeLeftLabel(getString(R.string.archive))
                    .setSwipeLeftLabelColor(Color.WHITE)
                    //.addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
                    //.addPadding(TypedValue.COMPLEX_UNIT_DIP, 8, 16, 8)
                    .create()
                    .decorate();

            // woring code

            /*final ColorDrawable background = new ColorDrawable(Color.RED);
            background.setBounds(0, viewHolder.itemView.getTop(), (int) (viewHolder.itemView.getLeft() + dX), viewHolder.itemView.getBottom());
            background.draw(c);

            Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.delete);
            // compute top and left margin to the view bounds
            icon.setBounds(0, viewHolder.itemView.getTop(), (int) (viewHolder.itemView.getLeft() + dX), viewHolder.itemView.getBottom());
            icon.draw(c);


            final ColorDrawable background1 = new ColorDrawable(Color.GREEN);
            background1.setBounds((int) (viewHolder.itemView.getRight() - dX), viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
            background1.draw(c);

            Drawable icon1 = ContextCompat.getDrawable(getContext(), R.drawable.delete);
            // compute top and left margin to the view bounds
            icon1.setBounds((int) (viewHolder.itemView.getRight() - dX), viewHolder.itemView.getTop(),viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
            icon1.draw(c);*/

         /* c.drawColor(Color.RED);
            Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            circlePaint.setColor(Color.RED);
            c.drawLine(c.getWidth()/2, c.getHeight()/2-200, c.getWidth()/2 ,c.getHeight()/2+200, circlePaint);
            c.drawLine(c.getWidth()/2-200, c.getHeight()/2, c.getWidth()/2+200 ,c.getHeight()/2, circlePaint); */
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public class viewpensql extends AsyncTask<String, Void, String> {
        String res = "";
        List<String> arrlist_name = new ArrayList<>();
        List<String> arrlist_type = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT)
                    .show();
        }


        // Draw the delete icon

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            PenFragment fr = new PenFragment();
            fr.onResume();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(variables.url, variables.user, variables.pass);
                System.out.println("Database connection success");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select serialnumber,orgId from pen_master where arstatus = '" + 0 + "' and orgid ='"+ orgId +"'");

                while (rs.next()) {
                    String pennamedb = rs.getString(1).trim();
                    arrlist_name.add(pennamedb);

                    String pentypedb = rs.getString(2).trim();
                    arrlist_type.add(pentypedb);
                    viewpenmodel ob1 = new viewpenmodel(pennamedb, pentypedb, "50%");
                    ob1.setName(pennamedb);
                    dataholder.add(ob1);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new viewpenAdapter(dataholder));
                    }
                });

                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    dataholder.clear();


                    viewpensql ob = new viewpensql();
                    ob.execute();

                    Toast.makeText(getContext(), "Reloaded ", Toast.LENGTH_SHORT)
                            .show();
                    // getFragmentManager().beginTransaction().detach(PenFragment.this).attach(PenFragment.this).commit();

                    swipeRefreshLayout.setRefreshing(false);
                    // recyclerView.setAdapter(new viewpenAdapter(dataholder));
                }
            });

        }
    }
}