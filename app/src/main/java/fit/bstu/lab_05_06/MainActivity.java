package fit.bstu.lab_05_06;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;

import fit.bstu.lab_05_06.auth.AuthActivity;
import fit.bstu.lab_05_06.auth.AuthManager;
import fit.bstu.lab_05_06.db.firebase.FireBaseManager;
import fit.bstu.lab_05_06.db.sqllite.AsyncTasks.IAsyncWriteCompletion;
import fit.bstu.lab_05_06.db.sqllite.ProductsManager;
import fit.bstu.lab_05_06.models.Product.ProductFirebase;
import fit.bstu.lab_05_06.models.Product.ProductModel;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.MainActivityOfChain;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.architecture.ChainOfActivitiesController;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.count_fragment.CountInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.image_fragment.ImageInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.name_fragment.NameInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.price_fragment.PriceInputFragment;
import fit.bstu.lab_05_06.shared_modules.drill_down.DrillDownController;
import fit.bstu.lab_05_06.shared_modules.drill_down.DrillDownFragment;
import fit.bstu.lab_05_06.shared_modules.list_of_items.IListOfItemsBehavior;
import fit.bstu.lab_05_06.shared_modules.list_of_items.ListOfItems;

/**
 * Created by andre on 27.11.2017.
 */

public class MainActivity extends AppCompatActivity implements IListOfItemsBehavior, NavigationView.OnNavigationItemSelectedListener {

    AuthManager authManager;
    GoogleApiClient mGoogleApiClient;
    ListOfItems retrievedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer);
        FragmentManager fragmentManager = getFragmentManager();
        retrievedFragment = (ListOfItems)fragmentManager.findFragmentByTag("listViewFragment");
        if(retrievedFragment == null) {
            ListOfItems chainItem = new ListOfItems();
            retrievedFragment = chainItem;
            FragmentTransaction transManager = fragmentManager.beginTransaction();
            transManager.add(R.id.list_fragment, chainItem, "listViewFragment");
            transManager.commit();
        }
        retrievedFragment.setDelegate(this);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentTransaction transManager = fragmentManager.beginTransaction();
            transManager.add(R.id.drill_down_fragment, new DrillDownFragment());
            transManager.commit();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        retrievedFragment.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authManager = AuthManager.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        authManager = AuthManager.getInstance();
        return true;
    }

    private void logout() {
        authManager.logOut();
        Intent loginIntent = new Intent(this, AuthActivity.class);
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        } else {
            mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    try{
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            });
            mGoogleApiClient.connect();
        }

        startActivity(loginIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int k = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, MainActivityOfChain.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
               // drawer.onO
                return true;
        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Serializable createdProduct = data.getSerializableExtra(MainActivityOfChain.CREATED_KEY);
        Serializable editedProduct = data.getSerializableExtra(MainActivityOfChain.UPDATED_KEY);
        IAsyncWriteCompletion copmletion = () -> {
//            refreshListViewByCurrentOrder((newCursor) -> {
//                productAdapter.changeCursor(newCursor);
//                productAdapter.notifyDataSetChanged();
//            });
        };
        if (createdProduct != null) {
            ProductModel productModel = (ProductModel)createdProduct;
            String id = FireBaseManager.getDbReference().push().getKey();
            productModel.setIdentifier(id);
            ProductFirebase dbModel = productModel.getFirebaseInstance();
            dbModel.setUserEmail(authManager.getUserEmail());
            FireBaseManager.getDbReference().child(id).setValue(dbModel);
            //dbManager.insert(productModel, copmletion);
        }

        if (editedProduct != null) {
            ProductModel productModel = (ProductModel)editedProduct;
            FireBaseManager.getDbReference().child(productModel.getIdentifier()).setValue(productModel.getFirebaseInstance());
            //dbManager.update(productModel, copmletion);
        }
    }

    @Override
    public void itemDidSelected(ProductFirebase product) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, DrillDownController.class);
            intent.putExtra(DrillDownController.BUNDLE_ARGUMENT_KEY, ProductModel.newInstance(product));
            startActivity(intent);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DrillDownFragment chainItem = new DrillDownFragment();
            chainItem.setProductModel(ProductModel.newInstance(product));
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transManager = fragmentManager.beginTransaction();
            transManager.replace(R.id.drill_down_fragment, chainItem);
            transManager.addToBackStack(null);
            transManager.commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_collection:
                return true;
            case R.id.nav_cards:
                return true;
            case R.id.nav_list:
                return true;
            case R.id.nav_table:
                return true;
            case R.id.nav_logout:
                logout();
                return true;
            default:
                return false;

        }
    }
}
