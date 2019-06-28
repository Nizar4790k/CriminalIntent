package android.bignerdranch.com;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CrimeListFragment  extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView=(RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return  view;


    }

        private class CrimeHolder extends RecyclerView.ViewHolder{


                public  CrimeHolder(LayoutInflater inflater,ViewGroup parent){
                    super(inflater.inflate(R.layout.list_item_crime,parent,false));
                }

        }


        private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

            private List<Crime> mCrimes;

            public CrimeAdapter(List<Crime> crimes){

            }

            @NonNull
            @Override
            public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                LayoutInflater inflater = LayoutInflater.from(getActivity());


                return new CrimeHolder(inflater,viewGroup);
            }

            @Override
            public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        }

}
