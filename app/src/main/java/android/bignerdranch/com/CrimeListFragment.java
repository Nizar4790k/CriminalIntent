package android.bignerdranch.com;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment  extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private  CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private Button mNewCrimeButton;
    private TextView mTextViewEmptyCrime;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }





    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view;

        if(CrimeLab.get(getContext()).getCrimes().size()!=0) {
             view = inflater.inflate(R.layout.fragment_crime_list, container, false);

            mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
            mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallBack());
            itemTouchHelper.attachToRecyclerView(mCrimeRecyclerView);


            updateUI();

            if (savedInstanceState != null) {
                mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            }




        } else {

            view = inflater.inflate(R.layout.empty_crimes,container,false);

            Button button = (Button)view.findViewById(R.id.button);
            TextView textView = (TextView)view.findViewById(R.id.textView);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crime crime = new Crime();
                    CrimeLab.get(getActivity()).addCrime(crime);
                    Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                    startActivity(intent);
                }
            });


        }


        return view;
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();


        if(crimes.size()==0){
            return;
        }


        if(mAdapter==null){


            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);


            return;

        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();

        }

            updateSubtitle();

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
                    mDateTextView.setText(mCrime.getDate().toString());
                    mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);

                    String crimeStatus = mCrime.isSolved() ? getString(R.string.crime_report_solved) : getString(R.string.crime_report_unsolved);
                    mSolvedImageView.setContentDescription(crimeStatus);

                }

            @Override
            public void onClick(View v) {


                mCallbacks.onCrimeSelected(mCrime);

            }
        }


        private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

            private List<Crime> mCrimes;


            public void setCrimes(List<Crime> crimes) {
                mCrimes = crimes;
            }

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

        @Override
        public void onResume(){
            super.onResume();
          updateUI();
        }


         @Override

         public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

             MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);

             if(mSubtitleVisible){

                 subtitleItem.setTitle(R.string.hide_subtitle);
             }else {

                 subtitleItem.setTitle(R.string.show_subtitle);
             }


         }


         public boolean onOptionsItemSelected(MenuItem item){

            switch (item.getItemId()){
                case R.id.new_crime:
                    Crime crime = new Crime();
                    CrimeLab.get(getActivity()).addCrime(crime);

                    updateUI();
                    mCallbacks.onCrimeSelected(crime);

                    return true;

                case  R.id.show_subtitle:
                    mSubtitleVisible=!mSubtitleVisible;
                    getActivity().invalidateOptionsMenu();

                    updateSubtitle();
                    return true;

                    default:
                        return super.onOptionsItemSelected(item);

            }






         }



                private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,crimeCount,crimeCount);



        if(!mSubtitleVisible){
            subtitle=null;
        }


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);


        }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    private class SwipeToDeleteCallBack extends ItemTouchHelper.SimpleCallback {



        public SwipeToDeleteCallBack() {
            super(0, ItemTouchHelper.RIGHT);

        }


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int position = viewHolder.getAdapterPosition();
            Crime crime =  mAdapter.mCrimes.get(position);

            CrimeLab crimeLab = CrimeLab.get(getContext());
            crimeLab.deleteCrime(crime);
            mAdapter.mCrimes.remove(crime);
            mAdapter.notifyItemRemoved(position);


        }


    }




}



