package android.bignerdranch.com;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends  SingleFragmentActivity{




    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
