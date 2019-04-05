package dd186.unifood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dd186.unifood.Entities.Deal;
import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Order;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Fragments.AccountFragment;
import dd186.unifood.Fragments.BasketFragment;
import dd186.unifood.Fragments.CardInfoFragment;
import dd186.unifood.Fragments.FavouritesFragment;
import dd186.unifood.Fragments.OffersFragment;
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
    public int basketItemsNum = 0;
    public TextView basketNumTextView;
    int qInt =1;
    SharedPreferences userDetails;
    public static Order pendingOrder = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDetails = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String extra = getIntent().getStringExtra("user");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user = objectMapper.readValue(extra, new TypeReference<User>() {});
            products = extractProductsFromJson(makeHttpRequest("http://10.0.2.2:8080/rest/products"));
            offers = objectMapper.readValue(makeHttpRequest("http://10.0.2.2:8080/rest/offers"), new TypeReference<List<Offer>>() {});
            deals = objectMapper.readValue(makeHttpRequest("http://10.0.2.2:8080/rest/deals"), new TypeReference<List<Deal>>() {});
            favourites = user.getFavouriteProducts();
            String basketStr = userDetails.getString("basket", "");
            assert basketStr != null;
            if (!basketStr.equals("")){
                basket = objectMapper.readValue(basketStr, new TypeReference<List<Product>>(){});
                int counter = 0;
                for (Product p:basket) {
                    if (p.getQuantity()>1){
                        p.setQuantity(p.getQuantity()-p.getQuantityInBasket());
                        counter++;
                    }else{
                        basket.remove(p);
                    }
                }
                setBasketBadgeNum(counter);
            }
        } catch (Exception e) {
            System.out.println("Something wrong with the deserialisationdd");
            e.printStackTrace();
        }
        orders = user.getOrders();
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
        ImageView basket = view.findViewById(R.id.basket_img);
        basket.setOnClickListener(v -> {
            Fragment fragment = new BasketFragment();
            loadFragment(fragment, "basket");
        });
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Fragment fragment = null;
//        switch (item.getItemId()) {
//            case R.id.basket_actionbar:
//                fragment = new BasketFragment();
//                break;
//        }
//        return loadFragment(fragment);
//    }

    public boolean loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
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
                args.putSerializable("products", (Serializable) products);
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
        runOnUiThread(() -> {
            if (basketItemsNum == 0) {
                basketNumTextView.setVisibility(View.INVISIBLE);
            } else {
                basketNumTextView.setVisibility(View.VISIBLE);
                basketNumTextView.setText(Integer.toString(basketItemsNum));
            }
        });
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

    //method used to make http requests
    public String makeHttpRequest(String link) throws ExecutionException, InterruptedException {
        HttpGetRequest httpGetRequest = new HttpGetRequest();
        httpGetRequest.setLink(link);
        httpGetRequest.execute();
        return httpGetRequest.get();
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
        if (!basket.isEmpty()){
            Gson googleJson = new Gson();
            String basketInString = googleJson.toJson(basket);
            SharedPreferences.Editor edit = userDetails.edit();
            edit.putString("basket", basketInString);
            edit.apply();
            for (Product p:basket) {
                p.setQuantity(p.getQuantity()+p.getQuantityInBasket());
            }
        }
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    //method to display all the sandwiches
    public void sandwiches(View view) {
        List<Product> sandwiches = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().getCategory().equals("Sandwiches")) {
                sandwiches.add(p);
            }
        }

        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) sandwiches);
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method to display all the snacks
    public void snacks(View view) {
        List<Product> snacks = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().getCategory().equals("Snacks")) {
                snacks.add(p);
            }
        }
        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) snacks);
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method to display all the drinks
    public void drinks(View view) {
        List<Product> drinks = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().getCategory().equals("Drinks")) {
                drinks.add(p);
            }
        }
        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) drinks);
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method to display all the sandwiches
    public void coffee(View view) {
        List<Product> coffee = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().getCategory().equals("Coffee")) {
                coffee.add(p);
            }
        }
        Fragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) coffee);
        fragment.setArguments(args);
        loadFragment(fragment, "search");
    }

    //method used to display all the set offers
    public void setOfOffers(View view){
        Fragment fragment = new OffersFragment();
        loadFragment(fragment, "offers");
    }

    //method used to display all the set offers
    public void payByCardPage(View view){
        Fragment fragment = new CardInfoFragment();
        loadFragment(fragment, "cardInfo");
    }

    //ALL METHODS RELATED TO THE BASKET'S FUNCTIONALITIES //

    //method for the button add to the basket products
    public void addToBasket(View view) {
        TextView productName = findViewById(R.id.product_name);
        TextView quantity = (TextView) findViewById(
                R.id.amount_quantity);
        Product product = new Product();
        for (Product p : products) {
            if (p.getName().contentEquals(productName.getText())) {
                product = p;
                break;
            }
        }
        int quantityInBasket = Integer.parseInt(quantity.getText().toString());
        product.setQuantity(product.getQuantity()-quantityInBasket);
        boolean existsInBasket = false;
        for (Product p: basket) {
            if (p.getId() == product.getId()) {
                int previousQuantity = p.getQuantityInBasket();
                p.setQuantityInBasket( previousQuantity + quantityInBasket);
                existsInBasket = true;
            }
        }
        if (!existsInBasket){
            product.setQuantityInBasket(quantityInBasket);
            basket.add(product);
        }
        int num=0;
        for (Product p:basket) {
            num += p.getQuantityInBasket();
        }
        Fragment reload = new HomeFragment();
        loadFragment(reload, "home");
        setBasketBadgeNum(num);
        Toast.makeText(getApplicationContext(),  "Added to the basket!", Toast.LENGTH_SHORT).show();
    }

    //method for the button add to the basket products of an offer
    public void addOfferToBasket(Offer offer) {
        offer.setQuantityInBasket(offer.getQuantityInBasket()+1);
        for (Product productOffer: offer.getProductsInOffer()) {
            Product product = new Product();
            for (Product p : products) {
                if (productOffer.getId() == p.getId()) {
                    product = p;
                    break;
                }
            }
            int previousQ = product.getQuantity();
            product.setQuantity(previousQ-1);
            boolean existsInBasket = false;
            for (Product p: basket) {
                if (p.getId() == product.getId()) {
                    int previousQuantityInBasket = p.getQuantityInBasket();
                    p.setQuantityInBasket(previousQuantityInBasket + 1);
                    existsInBasket = true;
                }

            }
            if (!existsInBasket){
                product.setQuantityInBasket(1);
                basket.add(product);
            }
        }
        int num=0;
        for (Product p:basket) {
            num += p.getQuantityInBasket();
        }
        Fragment reload = new OffersFragment();
        loadFragment(reload, "offers");
        setBasketBadgeNum(num);
        Toast.makeText(getApplicationContext(),  "Added to the basket!", Toast.LENGTH_SHORT).show();
    }

    //method used to remove the product from the basket
    public void removeFromBasket(Product product, TextView basketNumTextView) {
        this.basketNumTextView = basketNumTextView;
        basket.remove(product);
        product.setQuantity(product.getQuantity()+product.getQuantityInBasket());
        product.setQuantityInBasket(0);
        for (Offer o:offers) {
            if(o.getProductsInOffer().contains(product) && o.getQuantityInBasket()>0){
                o.setQuantityInBasket(0);
            }
        }
        int num=0;
        for (Product p:basket) {
            num += p.getQuantityInBasket();
        }
        setBasketBadgeNum(num);
    }

    public TextView getBasketNumTextView(){
        return basketNumTextView;
    }

    //on click method for pay in cash
    public void payInCash(View view) throws IOException, ExecutionException, InterruptedException {
        TextView finalTotal = findViewById(R.id.finalTotal_basket);
        HashMap<String,String> id_quanity = new HashMap<>();
        for (Product p:basket) {
            id_quanity.put(String.valueOf(p.getId()), String.valueOf(p.getQuantityInBasket()));
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson =  gsonBuilder.create();
        String json = gson.toJson(id_quanity);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpPostRequest httpPostRequest =  new HttpPostRequest();
        httpPostRequest.execute("http://10.0.2.2:8080/rest/addOrder/cash/"+user.getId() + "/"+String.valueOf(finalTotal.getText()), json);
        pendingOrder = objectMapper.readValue(httpPostRequest.get(), new TypeReference<Order>() {});
        afterPlaceOrder();
    }

    //METHODS RELATED TO THE QUANTITY OF THE PRODUCTS//

    //methods used to increase and decrease the quantity of the items in the basket
    public void increaseInteger(View view) {
        TextView productName = findViewById(R.id.product_name);
        Product product = new Product();
        for (Product p : products) {
            if (p.getName().contentEquals(productName.getText())) {
                product = p;
                break;
            }
        }
        if(product.getQuantity()>= qInt +1) {
            qInt++;}
        display(qInt);
    }

    public void decreaseInteger(View view) {
        if (qInt > 1)
            qInt = qInt - 1;
        display(qInt);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.amount_quantity);
        displayInteger.setText("" + number);
    }

    //method used to reset the quantity of the items in product info page
    public void resetQuantityInt(){
        qInt = 1;
    }

    //  USEFUL METHODS //

    public void afterPlaceOrder(){
        basket = new ArrayList<>();
        setBasketBadgeNum(0);
        Fragment fragment =  new OrderStatusFragment();
        Bundle args = new Bundle();
        //it's the first time the orderStatusFragment is called so the 1 stands for true
        args.putInt("firstTime", 1);
        fragment.setArguments(args);
        loadFragment(fragment, "orderStatus");
        Toast.makeText(getApplicationContext(), "Order sent!", Toast.LENGTH_SHORT).show();
        FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(user.getId()));
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
}