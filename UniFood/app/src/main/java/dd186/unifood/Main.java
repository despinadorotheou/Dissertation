package dd186.unifood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import dd186.unifood.Entities.Deal;
import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Order;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Fragments.AccountFragment;
import dd186.unifood.Fragments.BasketFragment;
import dd186.unifood.Fragments.FavouritesFragment;
import dd186.unifood.Fragments.OffersFragment;
import dd186.unifood.Fragments.ProductInfoFragment;
import dd186.unifood.Fragments.SearchFragment;
import dd186.unifood.Fragments.OrderHistoryFragment;
import dd186.unifood.Fragments.OrderStatusFragment;
import dd186.unifood.Fragments.HomeFragment;

public class Main extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static List<Product> products = new ArrayList<>();
    public static User user;
    public static List<Product> basket = new ArrayList<>();
    public static List<Offer> offers = new ArrayList<>();
    public static List<Deal> deals = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();
    public static List<Product> favourites = new ArrayList<>();
    public static boolean editing = false;
    public static MakeARequest makeARequest = new MakeARequest();
    public static String searchPageFilter = "No Filter";
    public int basketItemsNum = 0;
    public TextView basketNumTextView;
    SharedPreferences userDetails;
    public static Order pendingOrder = null;
    public TextView status;
    public LinearLayout edit_delete;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDetails = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String extra = getIntent().getStringExtra("user");
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            user = objectMapper.readValue(extra, new TypeReference<User>() {
                });
            refreshProducts();
            String response;
            if (!(response = makeARequest.get("http://10.0.2.2:8080/rest/deals")).isEmpty()) {
                deals = objectMapper.readValue(response, new TypeReference<List<Deal>>() {
                });
            }
            if (!(response = makeARequest.get("http://10.0.2.2:8080/rest/offers")).isEmpty()) {
                offers = objectMapper.readValue(response, new TypeReference<List<Offer>>() {
                });
            }

            String basketStr = userDetails.getString("basket", "");
            assert basketStr != null;
            if (!basketStr.equals("")){
                basket = objectMapper.readValue(basketStr, new TypeReference<List<Product>>(){});
                int counter = 0;
                for (Product p:basket) {
                    //checks the actual product's quantity
                    p = checkProduct(p);
                    if (p.getQuantity()>1){
                        p.setQuantity(p.getQuantity()-p.getQuantityInBasket());
                        makeARequest.get("http://10.0.2.2:8080/rest/decreaseQuantity/" + p.getId() + "/" + p.getQuantityInBasket());
                        counter++;
                    }else{
                        basket.remove(p);
                    }

                }
                setBasketBadgeNum(counter);
            }
        } catch (Exception e) {
            System.out.println("Something wrong with the deserialisation");
            e.printStackTrace();
        }
            if (user.getFavouriteProducts() != null)
                favourites = user.getFavouriteProducts();
            if (user.getOrders() != null)
                orders = user.getOrders();
        swipeRefreshLayout.setOnRefreshListener(() -> {
                refreshProducts();
            Object current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (current instanceof SearchFragment){
                SearchFragment fragment = (SearchFragment) current;
                Bundle args = new Bundle();
                args.putSerializable("filter",searchPageFilter );
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.detach(fragment);
                fragmentTransaction.attach(fragment);
                fragmentTransaction.commit();
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment(), "home");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.basket_actionbar);
        menuItem.setActionView(R.layout.basket_count);
        View view = menuItem.getActionView();
        basketNumTextView = (TextView) view.findViewById(R.id.basket_num);
        setBasketBadgeNum(basketItemsNum);
        return true;
    }

    public boolean loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
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
        String tag = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                tag = "home";
                break;
            case R.id.navigation_search:
                Bundle args = new Bundle();
                searchPageFilter = "None";
                args.putSerializable("filter",searchPageFilter );
                fragment = new SearchFragment();
                fragment.setArguments(args);
                tag = "search";
                break;
            case R.id.navigation_account:
                fragment = new AccountFragment();
                tag = "account";
                break;
        }
        return loadFragment(fragment, tag);
    }

    public void setBasketBadgeNum(int num) {
        basketItemsNum = num;
        if (basketNumTextView == null) return;
        if (basketItemsNum == 0) {
            basketNumTextView.setVisibility(View.INVISIBLE);
        } else {
            basketNumTextView.setVisibility(View.VISIBLE);
            basketNumTextView.setText(Integer.toString(basketItemsNum));
        }
    }

    //METHODS USED FOR THE HTTP REQUESTS

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

    //ON CLICK METHODS//

    //method for the order status button in the account fragment_search
    public void orderStatus(View view) {
        Fragment fragment = new OrderStatusFragment();
        Bundle args = new Bundle();
        //it's not the first time the orderStatusFragment is called so the 0 stands for false
        args.putInt("firstTime", 0);
        fragment.setArguments(args);
        loadFragment(fragment, "orderStatus");
    }

    //method for the order history button in the account fragment_search
    public void orderHistory(View view) {
        Fragment fragment = new OrderHistoryFragment();
        loadFragment(fragment, "orderHistory");
    }

    //method for the favourites button in the account fragment_search
    public void favourites(View view) {
        Fragment fragment = new FavouritesFragment();
        loadFragment(fragment, "favourites");
    }

    //Method for the sign out button so the user can return back to login page
    public void exit(View view) {
        SharedPreferences.Editor edit = userDetails.edit();
        if (!basket.isEmpty()){
            Gson googleJson = new Gson();
            String basketInString = googleJson.toJson(basket);
            edit.putString("basket", basketInString);
            edit.apply();
            for (Product p:basket) {
                p.setQuantity(p.getQuantity()+p.getQuantityInBasket());
                makeARequest.get("http://10.0.2.2:8080/rest/increaseQuantity/" + p.getId() + "/" + p.getQuantityInBasket());
            }
        } else {
            if(!Objects.requireNonNull(userDetails.getString("basket", "")).equals("") ){
                edit.remove("basket");
                edit.apply();
            }
        }
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    //method to display all the sandwiches
    public void sandwiches(View view) {
        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable("filter",  "Sandwiches");
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method to display all the snacks
    public void snacks(View view) {
        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        searchPageFilter = "Snacks";
        args.putSerializable("filter",searchPageFilter  );
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method to display all the drinks
    public void drinks(View view) {
        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        searchPageFilter = "Drinks";
        args.putSerializable("filter", searchPageFilter );
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method to display all the sandwiches
    public void coffee(View view) {
        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        searchPageFilter = "Coffee";
        args.putSerializable("filter",  searchPageFilter);
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method used to display all the set offers
    public void setOfOffers(View view){
        Fragment fragment = new OffersFragment();
        loadFragment(fragment, "offers");
    }
    //method used for the basket
    public void basketPage(View view){
        Fragment fragment = new BasketFragment();
        loadFragment(fragment, "basket");
    }


    //ALL METHODS RELATED TO THE BASKET'S FUNCTIONALITIES //

    //method for the button add to the basket products of an offer
    public void addOfferToBasket(Offer offer) throws ExecutionException, InterruptedException {
        refreshProducts();
        boolean available = true;
        for (Product productOffer: offer.getProductsInOffer()) {
            if (productOffer.getQuantity() <= 2) {
                available = false;
                break;
            }
        }
        if (available) {
            offer.setQuantityInBasket(offer.getQuantityInBasket() + 1);
            for (Product productOffer : offer.getProductsInOffer()) {
                Product product = new Product();
                for (Product p : products) {
                    if (productOffer.getId() == p.getId()) {
                        product = p;
                        break;
                    }
                }
                int previousQ = product.getQuantity();
                product.setQuantity(previousQ - 1);
                makeARequest.get("http://10.0.2.2:8080/rest/decreaseQuantity/" + product.getId() + "/1");
                boolean existsInBasket = false;
                for (Product p : basket) {
                    if (p.getId() == product.getId()) {
                        int previousQuantityInBasket = p.getQuantityInBasket();
                        p.setQuantityInBasket(previousQuantityInBasket + 1);
                        existsInBasket = true;
                    }

                }
                if (!existsInBasket) {
                    product.setQuantityInBasket(1);
                    basket.add(product);
                }
            }

            Fragment reload = new OffersFragment();
            loadFragment(reload, "offers");
            countBasketBadge();
            Toast.makeText(getApplicationContext(), "Added to the basket!", Toast.LENGTH_SHORT).show();
        } else {
            Fragment home = new HomeFragment();
            loadFragment(home, "home");
            Toast.makeText(getApplicationContext(), "Sorry offer out of stock!", Toast.LENGTH_SHORT).show();
        }
    }

    //method used to remove the product from the basket
    public void removeFromBasket(Product product, TextView basketNumTextView) {
        this.basketNumTextView = basketNumTextView;
        basket.remove(product);
        makeARequest.get("http://10.0.2.2:8080/rest/increaseQuantity/" + product.getId() + "/" + product.getQuantityInBasket());
        product.setQuantity(product.getQuantity()+product.getQuantityInBasket());
        product.setQuantityInBasket(0);
        for (Offer o:offers) {
            if(productInOffer(o,product) && o.getQuantityInBasket()>0){
                o.setQuantityInBasket(0);
            }
        }
        countBasketBadge();
    }

    public TextView getBasketNumTextView(){
        return basketNumTextView;
    }

    //on click method for the button edit in the order status fragment
    public void editOrder(View view) throws ExecutionException, InterruptedException {
        //to inform the admin that the order is being edited by the user
        makeARequest.get("http://10.0.2.2:8080/rest/editingOrder/" + pendingOrder.getId());
        basket = fromMapToList(pendingOrder.getProducts());
        countBasketBadge();
        Fragment fragment = new BasketFragment();
        loadFragment(fragment, "basket");
        editing = true;
    }

    //on click method for the btn delete
    public void deleteOrder(View view) throws ExecutionException, InterruptedException {
        //to inform the admin that the user wants to delete the order
        makeARequest.get("http://10.0.2.2:8080/rest/deleteOrder/" + pendingOrder.getId());
        //stop the timer
        OrderStatusFragment.t.cancel();
        Fragment fragment = new HomeFragment();
        loadFragment(fragment,"home");
        pendingOrder = null;
        Toast.makeText(getApplicationContext(),  "Order successfully deleted!", Toast.LENGTH_SHORT).show();
        refreshProducts();
    }

    //  USEFUL METHODS //

    //method used to setup everything after placing an order
    public void afterPlaceOrder(){
        basket = new ArrayList<>();
        setBasketBadgeNum(0);
        Fragment fragment =  new OrderStatusFragment();
        Bundle args = new Bundle();
        if (editing){
            args.putInt("firstTime", 0);
        }else {
            //it's the first time the orderStatusFragment is called so the 1 stands for true
            args.putInt("firstTime", 1);

        }
        fragment.setArguments(args);
        loadFragment(fragment, "orderStatus");
        Toast.makeText(getApplicationContext(), "Order sent!", Toast.LENGTH_SHORT).show();
        FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(user.getId()));
        editing = false;
    }


    //takes a map with the products ids and converts it to a list o products
    public List<Product> fromMapToList(HashMap<String, Integer> map){
        List<Product> list = new ArrayList<>();
        for (String i:map.keySet()) {
            Product product = null;
            for (Product p:products) {
                if (p.getId() == Integer.parseInt(i)){
                    product = p;
                    break;
                }
            }
            if (product != null) {
                product.setQuantityInBasket(map.get(i));
                list.add(product);
            }
        }
        return list;
    }

    public void countBasketBadge(){
        int num=0;
        for (Product p:basket) {
            num += p.getQuantityInBasket();
        }
        setBasketBadgeNum(num);
    }

    public void setStatus(TextView status) {
        this.status = status;
    }

    public void setTextStatus(String str){
        status.setText(str);
    }

    public void setEdit_delete(LinearLayout linearLayout){
        this.edit_delete = linearLayout;
    }

    public void setGoneEditDelete(){
        edit_delete.setVisibility(View.GONE);
    }

    public void refreshProducts()  {
        String response = makeARequest.get("http://10.0.2.2:8080/rest/products");
        products = extractProductsFromJson(response);
    }

    //method used to return the updated product from the list after refresh
    public Product checkProduct(Product product){
        for (Product p:products) {
            if (product.getId() == p.getId()){
                product = p;
                break;
            }

        }
        return product;
    }

    public void productInfoPage(Product product){
        refreshProducts();
        if (checkProduct(product).getQuantity()>=1){
            Fragment productView = new ProductInfoFragment();
            Bundle args =  new Bundle();
            args.putSerializable("product", (Serializable) product);
            productView.setArguments(args);
            loadFragment(productView, "productInfo");
        } else {
            Toast.makeText(getApplicationContext(), "Sorry product out of stock!", Toast.LENGTH_SHORT).show();
            Fragment home = new HomeFragment();
            loadFragment(home,"home");
        }
    }

    //checks whether the product is a part of a deal
    public boolean productInOffer(Offer offer, Product product){
        for (Product p:offer.getProductsInOffer()) {
            if (p.getId() == product.getId()){
                return true;
            }

        }
        return false;
    }


}