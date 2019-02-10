package dd186.unifood;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Fragments.AccountFragment;
import dd186.unifood.Fragments.BasketFragment;
import dd186.unifood.Fragments.CategoryFragment;
import dd186.unifood.Fragments.FavouritesFragment;
import dd186.unifood.Fragments.SearchFragment;
import dd186.unifood.Fragments.OrderHistoryFragment;
import dd186.unifood.Fragments.OrderStatusFragment;
import dd186.unifood.Fragments.HomeFragment;

public class Main extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    List<Product> products = new ArrayList<>();
    User user;
    List<Product> basket = new ArrayList<>();
    List<Product> favourites = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String extra = getIntent().getStringExtra("user");
        ObjectMapper objectMapper =  new ObjectMapper();
        try {
            user = objectMapper.readValue(extra, new TypeReference<User>() {
            });

        } catch (Exception e) {
            System.out.println("Something wrong with the deserialisation of products ");
            e.printStackTrace();
        }
        try {
            products = extractProductsFromJson(makeHttpRequest("http://10.0.2.2:8080/rest/products"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        favourites = user.getFavouriteProducts();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.basket:
                fragment = new BasketFragment();
                break;
        }
        return loadFragment(fragment);
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

    //method for the order status button in the account fragment_search
    public void orderStatus(View view){
        Fragment fragment = new OrderStatusFragment();
        loadFragment(fragment);
    }

    //method for the order history button in the account fragment_search
    public void orderHistory(View view){
        Fragment fragment = new OrderHistoryFragment();
        loadFragment(fragment);
    }

    //method for the favourites button in the account fragment_search
    public void favourites(View view){
        Fragment fragment = new FavouritesFragment();
        loadFragment(fragment);
    }

    //Method for the sign out button so the user can return back to login page
    public void exit(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    //method used to send the list of products to the fragments
    public List<Product> getProducts() {
        return products;
    }

    //method used to send the list of products that are in the basket to the fragments
    public List<Product> getBasketProducts() {
        return basket;
    }

    //method used to update the products in the basket
    public void setBasketProducts(List<Product> basketProducts){
        basket = basketProducts;
    }

    //method used to send the user to the fragments
    public  User getUser(){
        return user;
    }

    //method used to receive the updated user from the fragments
    public  void setUser(User user){
        this.user =  user;
    }

    //method used to send the user's favourite products
    public List<Product> getFavourites() {
        return favourites;
    }

    //method used to extract the products for the given json string
    public List<Product> extractProductsFromJson(String productString) {
        //if the json string is empty or null, the return early.
        ObjectMapper mapper = new ObjectMapper();
        if (TextUtils.isEmpty(productString)) {
            return null;
        }
        try {
            List<Product> products = new ArrayList<>();
            products = mapper.readValue(productString, new TypeReference<List<Product>>() {
            });

            return products;
        } catch (Exception e) {
            System.out.println("Something wrong with the deserialisation of products ");
            e.printStackTrace();
            return null;
        }
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

    //method used to do http requests
    public String makeHttpRequest(String link) throws ExecutionException, InterruptedException {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setLink(link);
        httpRequest.execute();
        return httpRequest.get();
    }

    //method for the button add to the basket
    public void addToBasket(View view){
        TextView productName = findViewById(R.id.product_name);
        Product product = new Product();
        for (Product p:products) {
            if (p.getName().contentEquals(productName.getText())){
                product = p;
                break;
            }
        }
        basket.add(product);
    }

 //method used to reload the empty basket page
    public void reloadEmptyBasketPage(){
//        ListView listView = findViewById(R.id.basket_list);
        Button payByCard = findViewById(R.id.card_checkout_btn);
        Button payByCash = findViewById(R.id.cash_checkout_btn);
        TextView total = findViewById(R.id.total_basket);
        TextView totalHeader =  findViewById(R.id.total_header);
        TextView empty = findViewById(R.id.empty_basket);
//        listView.setVisibility(View.INVISIBLE);
        payByCard.setVisibility(View.INVISIBLE);
        payByCash.setVisibility(View.INVISIBLE);
        total.setVisibility(View.INVISIBLE);
        totalHeader.setVisibility(View.INVISIBLE);
        empty.setVisibility(View.VISIBLE);
    }


}
