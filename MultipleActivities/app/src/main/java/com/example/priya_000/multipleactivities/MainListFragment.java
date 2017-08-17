package com.example.priya_000.multipleactivities;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class MainListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       ArrayAdapter adapter= ArrayAdapter.createFromResource(getActivity(),R.array.android_versions,android.R.layout.simple_list_item_1);
       setListAdapter(adapter);
        getListView().setOnItemClickListener(this);



    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        value=adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getActivity(),value,Toast.LENGTH_SHORT).show();

    }

}
