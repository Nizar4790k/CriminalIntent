package android.bignerdranch.com;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private String mSuspectNumber;



    public Crime(){
        this(UUID.randomUUID());
    }


    public Crime(UUID uuid) {
        mId = uuid;
        mDate = new Date(); //
    }

    public UUID getId() {
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }


    public  void setTime(int hours,int minutes,int seconds){
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);
        cal.set(Calendar.HOUR_OF_DAY,hours);
        cal.set(Calendar.MINUTE,minutes);
        cal.set(Calendar.SECOND,seconds);
        mDate=cal.getTime();


    }


    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getSuspectNumber() {
        return mSuspectNumber;
    }

    public void setSuspectNumber(String suspectNumber) {
        mSuspectNumber = suspectNumber;
    }

    public String getPhotoFileName(){
        return "IMG_"+getId().toString()+".jpg";
    }

}
