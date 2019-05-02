package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dd186.unifood.Adapters.CheckoutProductAdapter;
import dd186.unifood.Entities.Category;
import dd186.unifood.Entities.Deal;
import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Order;
import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.MakeARequest;
import dd186.unifood.R;

import static dd186.unifood.Main.basket;
import static dd186.unifood.Main.editing;
import static dd186.unifood.Main.makeARequest;
import static dd186.unifood.Main.pendingOrder;
import static dd186.unifood.Main.user;

public class BasketFragment extends Fragment {
    TextView badge;
    Main main;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_basket, null );
        main = (Main) getActivity();
        assert main != null;
        badge = main.getBasketNumTextView();
        ListView listView = rootView.findViewById(R.id.basket_list);
        Button payByCard = rootView.findViewById(R.id.card_checkout_btn);
        Button payByCash = rootView.findViewById(R.id.cash_checkout_btn);
        TableLayout tableLayout = rootView.findViewById(R.id.table_basket);
        TextView total = rootView.findViewById(R.id.total_basket);
        TextView empty = rootView.findViewById(R.id.empty_basket);
        TextView discount = rootView.findViewById(R.id.discount_basket);
        TextView finalTotal = rootView.findViewById(R.id.finalTotal_basket);
        Button confirmChanges = rootView.findViewById(R.id.confirm_changes_btn);


        //send the order to the web application
        payByCash.setOnClickListener(v -> {
            HashMap<String,String> id_quanity = new HashMap<>();
            for (Product p:basket) {
                id_quanity.put(String.valueOf(p.getId()), String.valueOf(p.getQuantityInBasket()));
            }
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson =  gsonBuilder.create();
            String json = gson.toJson(id_quanity);
            ObjectMapper objectMapper = new ObjectMapper();
            String response;
                if (!(response = makeARequest.post("http://10.0.2.2:8080/rest/addOrder/cash/"+user.getId() + "/"+String.valueOf(finalTotal.getText()), json)).isEmpty()) {
                    try {
                        pendingOrder =objectMapper.readValue(response,new TypeReference<Order>(){});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            main.afterPlaceOrder();
        });

        //navigate the user to the card info page
        payByCard.setOnClickListener(v -> {
            Fragment fragment = new CardInfoFragment();
            Bundle args = new Bundle();
            args.putDouble("total", Double.parseDouble(String.valueOf(finalTotal.getText())));
            fragment.setArguments(args);
            main.loadFragment(fragment, "cardInfo");
        });

        //confirm changes (inform the web application about the changes
        confirmChanges.setOnClickListener(v -> {
            HashMap<String,String> id_quanity = new HashMap<>();
            for (Product p:basket) {
                id_quanity.put(String.valueOf(p.getId()), String.valueOf(p.getQuantityInBasket()));
            }
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson =  gsonBuilder.create();
            String json = gson.toJson(id_quanity);
            ObjectMapper objectMapper = new ObjectMapper();
            String response;
            if (!(response = makeARequest.post("http://10.0.2.2:8080/rest/editedOrder/" + Main.pendingOrder.getId()+ "/"+String.valueOf(finalTotal.getText()), json)).isEmpty()) {
                try {
                    pendingOrder =objectMapper.readValue(response,new TypeReference<Order>(){});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            main.afterPlaceOrder();
        });


        if (!basket.isEmpty()) {
            listView.setAdapter(new CheckoutProductAdapter(main,  basket, listView,payByCard,payByCash,total,empty,badge,discount,finalTotal,tableLayout, this));

            NumberFormat formatter = new DecimalFormat("#0.00");
            double totalPrice = findTotal();
            total.setText(formatter.format(totalPrice));
            double discountValue = findDiscount();
            if (discountValue !=0 ){
                discount.setText(formatter.format(discountValue));

            }
            double totalWithDiscount = totalPrice - discountValue;
            finalTotal.setText(formatter.format(totalWithDiscount));
            empty.setVisibility(View.INVISIBLE);
            if (editing){
                confirmChanges.setVisibility(View.VISIBLE);
                payByCard.setVisibility(View.GONE);
                payByCash.setVisibility(View.GONE);
            }
        } else {
            listView.setVisibility(View.INVISIBLE);
            payByCard.setVisibility(View.INVISIBLE);
            payByCash.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
        }
        if (Main.pendingOrder != null && !editing){
            payByCard.setClickable(false);
            payByCash.setClickable(false);
        }


        return rootView;
    }

    //method used to calculate the total price of the products in the basket
    public double findTotal(){
        double totalPrice = 0;
        for (Product p : basket) {
            totalPrice += (p.getPrice()* p.getQuantityInBasket()) ;
        }
        return totalPrice;
    }

    //method used to calculate the discount
    public double findDiscount(){
        // temporary list used to test if any of the offers or the deals apply without modifying the original list(products)
        List<Product> tester = new ArrayList<>();
        for (Product p : basket) {
            for (int i = 0; i<p.getQuantityInBasket(); i++){
                tester.add(p);
            }
        }
        double discountValue=0;
        for (Offer o:Main.offers) {
            //checks whether the offer is in the basket
            if (o.getQuantityInBasket()>0){
                for (int c=0; c<o.getQuantityInBasket(); c++) {
                    double priceBeforeDiscount = 0;
                    for (Product p : o.getProductsInOffer()) {
                        tester = removeProduct(p.getId(),tester);
                        priceBeforeDiscount += p.getPrice();
                    }
                    double diff = priceBeforeDiscount - o.getValue();
                    discountValue += diff;
                }
            }
        }
        for (Deal d:Main.deals) {
                //list used to store products temporarily just to see if the deal applies
                List<Product> temp;
                //the number of the products in the deal
            int numOfProductsInDeal;
            do {
                temp = new ArrayList<>();
                numOfProductsInDeal = 0;
                for (Category c : d.getCategoriesInDeal()) {
                    for (int i = 0; i < c.getQuantityInDeal(); i++) {
                        for (Product p : tester) {
                            //checks whether the product's category is the one im looking for
                            if (p.getCategory().getId() == c.getId()) {
                                temp.add(p);
                                tester = removeProduct(p.getId(), tester);
                                break;
                            }
                        }
                    }
                    //add how many products for the specific category
                    numOfProductsInDeal += c.getQuantityInDeal();
                }
                //checks whether the temp list has enough products
                if (temp.size() == numOfProductsInDeal) {
                    double originalPrice = 0;
                    for (Product p : temp) {
                        originalPrice += p.getPrice();
                    }
                    double diff = originalPrice - d.getValue();
                    discountValue += diff;
                } else {
                    //reset tester because the deal does not applies
                    tester.addAll(temp);
                }
                //if check if the deal applies more than once
            } while (temp.size() == numOfProductsInDeal);
        }
        return discountValue;
    }

    //removes the product with that id from the list
    public List<Product> removeProduct(int id, List<Product> tester){
        for (Product p:tester) {
            if (p.getId()==id){
                tester.remove(p);
                break;
            }
        }
        return  tester;
    }




}
