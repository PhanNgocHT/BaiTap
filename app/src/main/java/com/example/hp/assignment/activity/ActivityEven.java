package com.example.hp.assignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hp.assignment.R;
import com.example.hp.assignment.custom.EvenAdapter;
import com.example.hp.assignment.database.DatabaseEvenImpl;
import com.example.hp.assignment.model.Even;

import java.util.ArrayList;
import java.util.List;

public class ActivityEven extends AppCompatActivity implements EvenAdapter.OnEvenClickListener {

    private RecyclerView rvEven;
    private TextView tvNotice;
    private List<Even> evens;
    private EvenAdapter adapter;
    private DatabaseEvenImpl database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evens);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        rvEven= (RecyclerView) findViewById(R.id.rv_evens);
        tvNotice= (TextView) findViewById(R.id.tv_notice);
    }

    private void initData() {
        evens=new ArrayList<>();
        adapter=new EvenAdapter(evens);
        database=new DatabaseEvenImpl(this);

        evens.addAll(database.getAllData());
        rvEven.setAdapter(adapter);
        if (evens.size()==0) {
            tvNotice.setText(R.string.notice);
        }
        rvEven.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        adapter.setOnEvenClickListener(this);
    }
    private void initEven() {
        evens.clear();
        evens.addAll(database.getAllData());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_add:
                Intent intent=new Intent(this, ActivityAddEven.class);
                startActivityForResult(intent, Constant.REQUEST_ADD);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constant.REQUEST_ADD||requestCode==Constant.REQUEST_EDIT) {
            tvNotice.setText("");
            initEven();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void clicked(int position) {
        Intent intent=new Intent(this, ActivityEditEven.class);
        intent.putExtra(Constant.POSITION, position);
        startActivityForResult(intent, Constant.REQUEST_EDIT);
    }

}
