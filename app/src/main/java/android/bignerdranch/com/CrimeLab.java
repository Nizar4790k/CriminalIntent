package android.bignerdranch.com;

import android.bignerdranch.com.database.CrimeBaseHelper;
import android.bignerdranch.com.database.CrimeCursorWrapper;
import android.bignerdranch.com.database.CrimeDbSchema;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab  {

    private static CrimeLab sCrimeLab;


        private  Context mContext;
        private SQLiteDatabase mDatabase;


    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab= new CrimeLab(context);
        }

        return sCrimeLab;
    }


    private  CrimeLab(Context context){

        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();




    }


    public void updateCrime (Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values, CrimeDbSchema.CrimeTable.Cols.UUID+ " = ?",
                new String []{uuidString});
    }



    public Crime getCrime(UUID id){

            CrimeCursorWrapper cursorWrapper = queryCrimes(
                    CrimeDbSchema.CrimeTable.Cols.UUID +" = ?",
                    new String[] {id.toString()}
                    );



            try{
                if(cursorWrapper.getCount()==0){
                    return null;
                }

                cursorWrapper.moveToFirst();
                return cursorWrapper.getCrime();
            }finally {

                cursorWrapper.close();
            }





    }

    public  List<Crime> getCrimes(){

       List<Crime> crimes = new ArrayList<>();

       CrimeCursorWrapper cursor = queryCrimes(null,null);

       try {
           cursor.moveToFirst();
           while (!cursor.isAfterLast()){
               crimes.add(cursor.getCrime());
               cursor.moveToNext();
           }
       } finally {
           cursor.close();
       }





        return  crimes;
    }


    public void addCrime(Crime c){

        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);

    }

    public void deleteCrime(Crime c){

        String uuid= c.getId().toString();

        mDatabase.delete(CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Cols.UUID+" = ?",
                new String[]{uuid});


    }


    private  static ContentValues getContentValues(Crime crime){

        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved()? 1:0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getSuspect());


        return values;

    }


    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }



}
