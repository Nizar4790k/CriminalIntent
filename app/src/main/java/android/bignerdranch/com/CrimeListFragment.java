package android.bignerdranch.com;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment  extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private  CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView=(RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return  view;


    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

        private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private ImageView  mSolvedImageView;
            private TextView mTitleTextView;
            private TextView mDateTextView;
            private Crime mCrime;

                public  CrimeHolder(LayoutInflater inflater,ViewGroup parent){
                    super(inflater.inflate(R.layout.list_item_crime,parent,false));

                    mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
                    mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
                    itemView.setOnClickListener(this);
                    mSolvedImageView =(ImageView) itemView.findViewById(R.id.crime_solved);
                }

                public void bind(Crime crime) {
                mCrime = crime;
                mTitleTextView.setText(mCrime.getTitle());
                mDateTextView.setText(DateFormat.format("mm-dd-yyyy",mCrime.getDate()));
                mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE: View.GONE);

                }

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                        .show();
            }
        }


        private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

            private List<Crime> mCrimes;

            public CrimeAdapter(List<Crime> crimes){
                mCrimes=crimes;

            }

            @NonNull
            @Override
            public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                LayoutInflater inflater = LayoutInflater.from(getActivity());


                return new CrimeHolder(inflater,viewGroup);
            }

            @Override
            public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {

                Crime crime = mCrimes.get(position);
                holder.bind(crime);

            }

            @Override
            public int getItemCount() {
                return mCrimes.size();
            }
        }


}
