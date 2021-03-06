package com.jshvarts.shoppinglist.lobby;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.jshvarts.shoppinglist.R;
import com.jshvarts.shoppinglist.lobby.fragments.ViewShoppingListFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LobbyActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            // Activity has not been recreated
            attachViewShoppingListFragment();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment shoppingListFragment = getSupportFragmentManager().findFragmentByTag(ViewShoppingListFragment.TAG);
        if (shoppingListFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            shoppingListFragment.getChildFragmentManager().popBackStack();
            return;
        }
        finish();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    private void attachViewShoppingListFragment() {
        Fragment shoppingListFragment = new ViewShoppingListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, shoppingListFragment, ViewShoppingListFragment.TAG)
                .addToBackStack(ViewShoppingListFragment.TAG)
                .commit();
    }

    private boolean isViewShoppingListFragmentAttached() {
        return getSupportFragmentManager().findFragmentByTag(ViewShoppingListFragment.TAG) != null;
    }
}
