package dd186.unifood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Product;
import dd186.unifood.Fragments.AccountFragment;
import dd186.unifood.Fragments.CategoryFragment;
import dd186.unifood.Fragments.FavouritesFragment;
import dd186.unifood.Fragments.HomeFragment;
import dd186.unifood.Fragments.OrderHistoryFragment;
import dd186.unifood.Fragments.OrderStatusFragment;
import dd186.unifood.Fragments.SearchFragment;

public class Main extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ProductAdapter productAdapter;
    private GridView productsView;
    List<Product> products = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if( intent.getExtras().getSerializable("products") != null){
            products = (List<Product>) intent.getExtras().getSerializable("products");
        } else
            products = null;
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());

    }


    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;
            case R.id.navigation_account:
                fragment = new AccountFragment();
                break;
        }
        return loadFragment(fragment);
    }

    //method for the order status button in the account fragment
    public void orderStatus(View view){
        Fragment fragment = new OrderStatusFragment();
        loadFragment(fragment);
    }

    //method for the order history button in the account fragment
    public void orderHistory(View view){
        Fragment fragment = new OrderHistoryFragment();
        loadFragment(fragment);
    }

    //method for the favourites button in the account fragment
    public void favourites(View view){
        Fragment fragment = new FavouritesFragment();
        loadFragment(fragment);
    }

    //Method for the sign out button so the user can return back to login page
    public void exit(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public List<Product> getProducts() {
        return products;
    }

    //method to display all the sandwiches
    public void sandwiches(View view){
        List<Product> sandwiches = new ArrayList<>();
        for (Product p:products) {
            if (p.getCategory().getCategory().equals("Sandwiches")){
                sandwiches.add(p);
            }
        }

        Fragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) sandwiches);
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    //method to display all the snacks
    public void snacks(View view){
        List<Product> snacks = new ArrayList<>();
        for (Product p:products) {
            if (p.getCategory().getCategory().equals("Snacks")){
                snacks.add(p);
            }
        }
        Fragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) snacks);
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    //method to display all the drinks
    public void drinks(View view){
        List<Product> drinks = new ArrayList<>();
        for (Product p:products) {
            if (p.getCategory().getCategory().equals("Drinks")){
                drinks.add(p);
            }
        }
        Fragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) drinks);
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    //method to display all the sandwiches
    public void coffee(View view){
        List<Product> coffee = new ArrayList<>();
        for (Product p:products) {
            if (p.getCategory().getCategory().equals("Coffee")){
                coffee.add(p);
            }
        }
        Fragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) coffee);
        fragment.setArguments(args);
        loadFragment(fragment);
    }


}