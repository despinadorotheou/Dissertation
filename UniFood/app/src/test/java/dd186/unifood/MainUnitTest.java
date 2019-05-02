package dd186.unifood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import org.apache.maven.wagon.ConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import dd186.unifood.Entities.Category;
import dd186.unifood.Entities.Deal;
import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Order;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Fragments.AccountFragment;
import dd186.unifood.Fragments.BasketFragment;
import dd186.unifood.Fragments.CardInfoFragment;
import dd186.unifood.Fragments.FavouritesFragment;
import dd186.unifood.Fragments.HomeFragment;
import dd186.unifood.Fragments.OffersFragment;
import dd186.unifood.Fragments.OrderHistoryFragment;
import dd186.unifood.Fragments.OrderStatusFragment;
import dd186.unifood.Fragments.ProductInfoFragment;
import dd186.unifood.Fragments.SearchFragment;

import static dd186.unifood.Main.makeARequest;
import static dd186.unifood.Main.pendingOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;



@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class MainUnitTest {

    private Main tested;

    private Category category;
    private Product product;

    @Before
    public void setUp(){
        //setup activity
        MockitoAnnotations.initMocks(this);
        String mockUserJson = "{\"id\":6,\"name\":\"Despina\",\"lastName\":\"Dorotheou\",\"email\":\"dd186@student.le.ac.uk\",\"password\":\"$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq\",\"favouriteProducts\":[25,26,2,9],\"orders\":[{\"id\":200,\"date\":\"Sat Apr 20 13:42:25 BST 2019\",\"value\":0.8,\"products\":{\"25\":1}},{\"id\":193,\"date\":\"Sat Apr 20 13:16:46 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":198,\"date\":\"Sat Apr 20 13:25:50 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":209,\"date\":\"Sat Apr 20 15:53:11 BST 2019\",\"value\":2.5,\"products\":{\"18\":1}},{\"id\":196,\"date\":\"Sat Apr 20 13:19:39 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":207,\"date\":\"Sat Apr 20 15:08:05 BST 2019\",\"value\":2.5,\"products\":{\"27\":1}},{\"id\":192,\"date\":\"Sat Apr 20 13:07:57 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":195,\"date\":\"Sat Apr 20 13:19:36 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":199,\"date\":\"Sat Apr 20 13:34:04 BST 2019\",\"value\":1.5,\"products\":{\"19\":1}},{\"id\":208,\"date\":\"Sat Apr 20 15:35:01 BST 2019\",\"value\":0.8,\"products\":{\"11\":1}},{\"id\":204,\"date\":\"Sat Apr 20 14:26:24 BST 2019\",\"value\":0.8,\"products\":{\"13\":1}},{\"id\":194,\"date\":\"Sat Apr 20 13:17:07 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":203,\"date\":\"Sat Apr 20 14:19:43 BST 2019\",\"value\":1.5,\"products\":{\"19\":1}},{\"id\":201,\"date\":\"Sat Apr 20 13:52:43 BST 2019\",\"value\":2.5,\"products\":{\"27\":1}},{\"id\":197,\"date\":\"Sat Apr 20 13:20:20 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":202,\"date\":\"Sat Apr 20 14:03:50 BST 2019\",\"value\":0.8,\"products\":{\"25\":1}},{\"id\":205,\"date\":\"Sat Apr 20 14:38:15 BST 2019\",\"value\":0.8,\"products\":{\"25\":1}},{\"id\":206,\"date\":\"Sat Apr 20 14:56:10 BST 2019\",\"value\":0.8,\"products\":{\"9\":1}},{\"id\":210,\"date\":\"Sat Apr 20 19:24:07 BST 2019\",\"value\":1.0,\"products\":{\"15\":1}}]}";
        Intent intent = new Intent();
        intent.putExtra("user", mockUserJson);
        makeARequest = mock(MakeARequest.class);
        when(makeARequest.get(anyString())).thenReturn("").thenReturn("").thenReturn("").thenReturn("");
        tested = spy(Robolectric.buildActivity(Main.class,intent).setup().get());
        //setup products;
        category = new Category("Drinks");
        category.setId(1);
        category.setQuantityInDeal(1);
        product = new Product(1, "milk", "semi",1.00, 50,category );
        product.setQuantityInBasket(1);
        product.setPreference("Vegan");
        Main.products = new ArrayList<>(Collections.singleton(product));
        //the product is in the basket list too
        Main.basket = new ArrayList<>(Collections.singleton(product));

        //setup offers
        Offer offer = new Offer("Mock offer 2", 1.00);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1", 2);
        offer.setProductsInOffer(map);
        Main.offers = new ArrayList<>(Collections.singleton(offer));

        //setup deals
        Deal deal = new Deal("Mock deal", 1.5);
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        deal.setCategoriesInDeal(categories);
        Main.deals = new ArrayList<>(Collections.singleton(deal));
    }

    //tests for the methods in the main activity//

    @Test
    public void searchFragment(){
        //test
        tested.findViewById(R.id.navigation_search).performClick();

        //verify
        SearchFragment searchFragment = (SearchFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(searchFragment);
        assertTrue(searchFragment.isVisible());

    }

    @Test
    public void homeFragment(){
        //test
        tested.findViewById(R.id.navigation_home).performClick();

        //verify
        HomeFragment homeFragment = (HomeFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(homeFragment);
        assertTrue(homeFragment.isVisible());

    }

    @Test
    public void accountFragment(){
        //test
        tested.findViewById(R.id.navigation_account).performClick();

        //verify
        AccountFragment accountFragment = (AccountFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(accountFragment);
        assertTrue(accountFragment.isVisible());

    }

    @Test
    public void testSetBasketBadgeNum(){

        //test
        tested.setBasketBadgeNum(3);
        //verify
        assertEquals("3",tested.basketNumTextView.getText());
    }

    @Test
    public void basketFragment(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.basket_count, null);
        ImageView imageView = view.findViewById(R.id.basket_img);

        //test
       imageView.performClick();

        //verify
        BasketFragment basketFragment = (BasketFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(basketFragment);
        assertTrue(basketFragment.isVisible());

    }

    @Test
    public void basketFragment1(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.basket_count, null);
        ImageView imageView = view.findViewById(R.id.basket_img);
        //with empty basket
        Main.basket = new ArrayList<>();

        //test
        imageView.performClick();

        //verify
        BasketFragment basketFragment = (BasketFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(basketFragment);
        assertTrue(basketFragment.isVisible());

    }

    @Test
    public void testExtractProductsFromJson(){
        //setup
        String mockProductJson = "[{\"id\":2,\"name\":\"Iced Coffee Mocha\",\"description\":\"Contains: Lactose, Milk.\",\"price\":1.55,\"quantity\":31,\"preference\":\"Vegetarian\",\"ingredients\":\"Milk, coffee, water, chocolate, sugar\",\"category\":{\"id\":4,\"category\":\"Coffee\"}}]";

        //test
        List<Product> products = tested.extractProductsFromJson(mockProductJson);

        //verify
        assertNotNull(products);
        assertEquals(1,products.size());

    }

    @Test
    public void testFailedExtractProductsFromJson(){
        //setup
        String mockProductJson = "[\"id\":2,\"name\":\"Iced Coffee Mocha\",\"description\":\"Contains: Lactose, Milk.\",\"price\":1.55,\"quantity\":31,\"preference\":\"Vegetarian\",\"ingredients\":\"Milk, coffee, water, chocolate, sugar\",\"category\":{\"id\":4,\"category\":\"Coffee\"}}]";

        //test
        List<Product> products = tested.extractProductsFromJson(mockProductJson);

        //verify
        assertNull(products);

    }

    @Test
    public void testOrderStatusPage(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_account, null);
        Button button = view.findViewById(R.id.orderStatus_btn);

        //test
        button.performClick();

        //verify
        OrderStatusFragment orderStatusFragment = (OrderStatusFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(orderStatusFragment);
        assertTrue(orderStatusFragment.isVisible());
    }

    @Test
    public void testOrderHistoryPage(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_account, null);
        Button button = view.findViewById(R.id.order_history_btn);

        //test
        button.performClick();

        //verify
        OrderHistoryFragment orderHistoryFragment = (OrderHistoryFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(orderHistoryFragment);
        assertTrue(orderHistoryFragment.isVisible());

    }

    @Test
    public void testFavouritePage(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_account, null);
        Button button = view.findViewById(R.id.favourites_btn);

        //test
        button.performClick();

        //verify
        FavouritesFragment favouritesFragment = (FavouritesFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(favouritesFragment);
        assertTrue(favouritesFragment.isVisible());

    }

    @Test
    public void testSignoutBtn(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_account, null);
        Button button = view.findViewById(R.id.signout_btn2);


        //test
        button.performClick();

        //verify
        ShadowActivity shadowActivity = shadowOf(tested);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getIntentClass().getName(), equalTo(Login.class.getName()));
    }

    @Test
    public void testSandwichCategory(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_home, null);
        Button button = view.findViewById(R.id.sandwich_category_btn);

        //test
        button.performClick();

        //verify
        SearchFragment searchFragment = (SearchFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(searchFragment);
        assertTrue(searchFragment.isVisible());
    }
    @Test
    public void testSnacksCategory(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_home, null);
        Button button = view.findViewById(R.id.snacks_category_btn);

        //test
        button.performClick();

        //verify
        SearchFragment searchFragment = (SearchFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(searchFragment);
        assertTrue(searchFragment.isVisible());
    }

    @Test
    public void testCoffeeCategory(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_home, null);
        Button button = view.findViewById(R.id.coffee_category_btn);

        //test
        button.performClick();

        //verify
        SearchFragment searchFragment = (SearchFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(searchFragment);
        assertTrue(searchFragment.isVisible());
    }

    @Test
    public void testDrinksCategory(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_home, null);
        Button button = view.findViewById(R.id.drinks_category_btn);

        //test
        button.performClick();

        //verify
        SearchFragment searchFragment = (SearchFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(searchFragment);
        assertTrue(searchFragment.isVisible());
    }

    @Test
    public void testSetOfferBtn(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_home, null);
        Button button = view.findViewById(R.id.setoffers_btn);

        //test
        button.performClick();

        //verify
        OffersFragment offersFragment = (OffersFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(offersFragment);
        assertTrue(offersFragment.isVisible());
    }

    @Test
    public void testAddOfferToTheBasket() throws ExecutionException, InterruptedException {
        //setup
        Offer offer = new Offer("Mock offer 2", 1.00);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1", 2);
        offer.setProductsInOffer(map);
        when(makeARequest.get(anyString())).thenReturn("[{\"id\":1,\"name\":\"milk\",\"description\":\"semi\",\"price\":1.0,\"quantity\":50,\"category\":{\"id\":1,\"category\":\"Drinks\"}}]");


        //test
        tested.addOfferToBasket(offer);

        //verify
        assertEquals(1,Main.basket.size());
        assertEquals(3, Main.basket.get(0).getQuantityInBasket());
        OffersFragment offersFragment = (OffersFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(offersFragment);
        assertTrue(offersFragment.isVisible());
    }

    @Test
    public void testRemoveFromTheBasket(){
        //test
        tested.removeFromBasket(product,tested.basketNumTextView);

        //verify
        assertTrue(Main.basket.isEmpty());
    }

    @Test
    public void testEditOrder(){
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_order_status, null);
        Button button = view.findViewById(R.id.edit_order_btn);
        Main.pendingOrder = new Order();
        Main.pendingOrder.setId(1);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1", 2);
        Main.pendingOrder.setProducts(map);
        Main.pendingOrder.setValue(2.00);
        doReturn("").when(makeARequest).get(anyString());


        //test
        button.performClick();

        //verify
        assertEquals(1,Main.basket.size());
        assertEquals(2, Main.products.get(0).getQuantityInBasket());
        assertTrue(Main.editing);
        BasketFragment basketFragment = (BasketFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(basketFragment);
        assertTrue(basketFragment.isVisible());
    }

    @Test
    public void testDeleteOrder()  {
        //setup
        LayoutInflater factory = tested.getLayoutInflater();
        View view = factory.inflate(R.layout.fragment_order_status, null);
        Button button = view.findViewById(R.id.delete_order_btn);
        Main.pendingOrder = new Order();
        Main.pendingOrder.setId(1);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1", 2);
        Main.pendingOrder.setProducts(map);
        Main.pendingOrder.setValue(2.00);
        //fake timer task just for the testing
        OrderStatusFragment.t = new Timer(false);
        OrderStatusFragment.t.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 0, 10000);
        when(makeARequest.get(anyString())).thenReturn("");


        //test
        button.performClick();

        //verify
        assertNull(Main.pendingOrder);
        HomeFragment homeFragment = (HomeFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(homeFragment);
        assertTrue(homeFragment.isVisible());
    }

    @Test
    public void testFromMapToList(){
        //setuo
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1", 2);
        //test
        List<Product> result = tested.fromMapToList(map);

        //verify
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getQuantityInBasket());
    }

    @Test
    public void testAfterPlaceOrder(){
        //setup
        FirebaseApp.initializeApp(tested.getApplicationContext());

        //test
        tested.afterPlaceOrder();

        //verify
        assertTrue(Main.basket.isEmpty());
        OrderStatusFragment orderStatusFragment = (OrderStatusFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(orderStatusFragment);
        assertTrue(orderStatusFragment.isVisible());
        assertFalse(Main.editing);
    }

    @Test
    public void testCountBasketBadge(){

        //test
        tested.countBasketBadge();

        //verify
        assertEquals("1",tested.basketNumTextView.getText());
    }

    // BASKET FRAGMENT //

    @Test
    public void testFindTotal(){
        //setUp
        BasketFragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();

        //test
        double total = fragment.findTotal();

        //verify
        assertEquals(1.00, total, 0.0);
    }

    @Test
    public void testFindDiscount(){
        //setUp
        BasketFragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Main.deals= new ArrayList<>();
        Main.offers.get(0).setQuantityInBasket(1);
        product.setQuantityInBasket(2);
        Main.basket = new ArrayList<>(Collections.singleton(product));


        //test
        double discount = fragment.findDiscount();

        //verify
        assertEquals(1.00, discount, 0.0);
    }

    @Test
    public void testFindDiscount1(){
        //setUp
        BasketFragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Category category2 = new Category("Snack");
        category2.setId(2);
        category2.setQuantityInDeal(1);
        Product product2 = new Product(2, "chips", "gr",0.80, 50,category2 );
        product2.setQuantityInBasket(1);
        Deal deal = new Deal("mock", 1.30);
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category2);
        deal.setCategoriesInDeal(categories);
        Main.deals= new ArrayList<>(Collections.singletonList(deal));
        Main.offers = new ArrayList<>();
        Main.basket.add(product2);


        //test
        double discount = fragment.findDiscount();

        //verify
        assertEquals(0.50, discount, 0.0);
    }

    @Test
    public void testPayByCardBtn(){
        //setUp
        BasketFragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Button button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.card_checkout_btn);

        //test
        button.performClick();

        //verify
        CardInfoFragment cardInfoFragment = (CardInfoFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(cardInfoFragment);
        assertTrue(cardInfoFragment.isVisible());
    }

    @Test
    public void testPayByCashBtn(){
        //setUp
        FirebaseApp.initializeApp(tested.getApplicationContext());
        BasketFragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Button button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.cash_checkout_btn);
        when(makeARequest.post(anyString(), anyString())).thenReturn("{\"id\":1,\"value\":\"1.55\",\"paid\":\"false\",\"products\":{\"1\": 2}}");


        //test
        button.performClick();

        //verify
        assertNotNull(pendingOrder);
        OrderStatusFragment orderStatusFragment = (OrderStatusFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(orderStatusFragment);
        assertTrue(orderStatusFragment.isVisible());
    }


    @Test
    public void testConfirmChanges(){
        //setUp
        Main.pendingOrder = new Order();
        Main.pendingOrder.setId(1);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1", 2);
        Main.pendingOrder.setProducts(map);
        Main.pendingOrder.setValue(2.00);
        FirebaseApp.initializeApp(tested.getApplicationContext());
        Fragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        when(makeARequest.post(anyString(), anyString())).thenReturn("{\"id\":1,\"value\":\"1.55\",\"paid\":\"false\",\"products\":{\"1\": 2}}");
        Button button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.confirm_changes_btn);


        //test
        button.performClick();

        //verify
        assertNotNull(pendingOrder);
        OrderStatusFragment orderStatusFragment = (OrderStatusFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(orderStatusFragment);
        assertTrue(orderStatusFragment.isVisible());
    }

    @Test
    public void testProductInfo(){
        //setUp
        BasketFragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        when(makeARequest.get(anyString())).thenReturn("[{\"id\":1,\"name\":\"milk\",\"description\":\"semi\",\"price\":1.0,\"quantity\":50,\"category\":{\"id\":1,\"category\":\"Drinks\"}}]");
        ListView listView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.basket_list);

        //test
        View view = getViewByPosition(0, listView);
        view.performClick();

        //verify
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(productInfoFragment);
        assertTrue(productInfoFragment.isVisible());
    }

    @Test
    public void testProductInfoOutOfStock(){
        //setUp
        BasketFragment fragment = new BasketFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        when(makeARequest.get(anyString())).thenReturn("[{\"id\":1,\"name\":\"milk\",\"description\":\"semi\",\"price\":1.0,\"quantity\":0,\"category\":{\"id\":1,\"category\":\"Drinks\"}}]");
        ListView listView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.basket_list);

        //test
        View view = getViewByPosition(0, listView);
        view.performClick();

        //verify
        HomeFragment homeFragment = (HomeFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(homeFragment);
        assertTrue(homeFragment.isVisible());
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    //CARD INFO FRAGMENT //

    @Test
    public void testConfirmDetailsBtn(){
        //setUp
        FirebaseApp.initializeApp(tested.getApplicationContext());
        CardInfoFragment fragment = new CardInfoFragment();
        Bundle args = new Bundle();
        args.putDouble("total", 2.0);
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Button button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.confirm_btn_card);
        when(makeARequest.post(anyString(), anyString())).thenReturn("{\"id\":1,\"value\":\"1.55\",\"paid\":\"true\",\"products\":{\"1\": 2}}");


        //test
        button.performClick();

        //verify
        assertNotNull(pendingOrder);
        OrderStatusFragment orderStatusFragment = (OrderStatusFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(orderStatusFragment);
        assertTrue(orderStatusFragment.isVisible());
    }

    // SEARCH FRAGMENT //

    @Test
    public void testVeganSearch(){
        //setUp
        SearchFragment fragment = new SearchFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString("filter", "None");
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Switch button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.vegan_switch);

        //test
        button.performClick();

        //verify
        GridView gridView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.product_list);
        assertEquals(product,(Product) gridView.getItemAtPosition(0));
    }

    @Test
    public void testVegetarianSearch(){
        //setUp
        SearchFragment fragment = new SearchFragment();
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString("filter", "None");
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        product.setPreference("Vegetarian");
        Main.products = new ArrayList<>(Collections.singletonList(product));
        Switch button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.vegetaterian_switch);

        //test
        button.performClick();

        //verify
        GridView gridView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.product_list);
        assertEquals(product,(Product) gridView.getItemAtPosition(0));
    }

    @Test
    public void testSearchByName(){
        //setUp
        Product product2 = new Product(2, "Water", "",0.80, 50,category );
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        Main.products = new ArrayList<>(products);
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("filter", "None");
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Spinner filter = Objects.requireNonNull(fragment.getView()).findViewById(R.id.spinner_filter);
        EditText userInput = Objects.requireNonNull(fragment.getView()).findViewById(R.id.search_input);

        //test
        filter.setSelection(0);
        userInput.setText("milk");

        //verify
        GridView gridView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.product_list);
        assertEquals(product,(Product) gridView.getItemAtPosition(0));
    }

    @Test
    public void testSearchByName2(){
        //setUp
        Product product2 = new Product(2, "Water", "",0.80, 50,category );
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        Main.products = new ArrayList<>(products);
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("filter", "None");
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Spinner filter = Objects.requireNonNull(fragment.getView()).findViewById(R.id.spinner_filter);
        EditText userInput = Objects.requireNonNull(fragment.getView()).findViewById(R.id.search_input);

        //test
        filter.setSelection(0);
        userInput.setText("w");
        userInput.setText("wa");
        userInput.setText("wat");
        userInput.setText("wate");
        userInput.setText("water");

        //verify
        GridView gridView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.product_list);
        assertEquals(product2,(Product) gridView.getItemAtPosition(0));
    }

    @Test
    public void testSearchByIngredients(){
        //setUp
        product.setIngredients("lactose");
        Product product2 = new Product(2, "Water", "",0.80, 50,category );
        product2.setIngredients("water");
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        Main.products = new ArrayList<>(products);
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("filter", "None");
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Spinner filter = Objects.requireNonNull(fragment.getView()).findViewById(R.id.spinner_filter);
        EditText userInput = Objects.requireNonNull(fragment.getView()).findViewById(R.id.search_input);

        //test
        filter.setSelection(1);
        userInput.setText("lactose");

        //verify
        GridView gridView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.product_list);
        assertEquals(product,(Product) gridView.getItemAtPosition(0));
    }

    @Test
    public void testSearchByIngredients2(){
        //setUp
        product.setIngredients("lactose");
        Product product2 = new Product(2, "Water", "",0.80, 50,category );
        product2.setIngredients("test");
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        Main.products = new ArrayList<>(products);
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("filter", "None");
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        Spinner filter = Objects.requireNonNull(fragment.getView()).findViewById(R.id.spinner_filter);
        EditText userInput = Objects.requireNonNull(fragment.getView()).findViewById(R.id.search_input);

        //test
        filter.setSelection(1);
        userInput.setText("tes");

        //verify
        GridView gridView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.product_list);
        assertEquals(product2,(Product) gridView.getItemAtPosition(0));
    }

    @Test
    public void testCostFilter(){
        //setUp
        Product product2 = new Product(2, "Water", "",3.00, 50,category );
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        Main.products = new ArrayList<>(products);
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("filter", "None");
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        SeekBar costFilter = Objects.requireNonNull(fragment.getView()).findViewById(R.id.cost_constain);

        //test
        costFilter.setProgress(10);
        costFilter.setProgress(1);

        //verify
        GridView gridView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.product_list);
        assertEquals(product,(Product) gridView.getItemAtPosition(0));
    }

    //PRODUCT INFO FRAGMENT//

    @Test
    public void testAddToBasket(){
        //setUp
        Main.basket = new ArrayList<>();
        Product product2 = new Product(2, "Water", "",3.00, 50,category );
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product2);
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        when(makeARequest.get(anyString())).thenReturn("[{\"id\":1,\"name\":\"milk\",\"description\":\"semi\",\"price\":1.0,\"quantity\":0,\"category\":{\"id\":1,\"category\":\"Drinks\"}}]");
        Button button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.add_basket_btn);

        //test
        button.performClick();

        //verify
        assertEquals(1, Main.basket.size());
        assertEquals(product2, Main.basket.get(0));
        HomeFragment homeFragment = (HomeFragment) tested.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assertNotNull(homeFragment);
        assertTrue(homeFragment.isVisible());
    }


    @Test
    public void testIncreaseQuantity(){
        //setUp
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        TextView quantity = Objects.requireNonNull(fragment.getView()).findViewById(R.id.amount_quantity);
        Button button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.increase_quantity_btn);

        //test
        button.performClick();

        //verify
       assertEquals("2", quantity.getText());
    }

    @Test
    public void testIncreaseQuantity2(){
        //setUp
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        TextView quantity = Objects.requireNonNull(fragment.getView()).findViewById(R.id.amount_quantity);
        Button button = Objects.requireNonNull(fragment.getView()).findViewById(R.id.increase_quantity_btn);

        //test
        button.performClick();
        button.performClick();


        //verify
        assertEquals("3", quantity.getText());
    }

    @Test
    public void testDecreaseQuantity(){
        //setUp
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        FragmentManager fragmentManager = tested.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
        TextView quantity = Objects.requireNonNull(fragment.getView()).findViewById(R.id.amount_quantity);
        Button increase = Objects.requireNonNull(fragment.getView()).findViewById(R.id.increase_quantity_btn);
        Button decrease = Objects.requireNonNull(fragment.getView()).findViewById(R.id.decrease_quantity_btn);

        //test
        increase.performClick();
        increase.performClick();
        decrease.performClick();


        //verify
        assertEquals("2", quantity.getText());
    }


}
