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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import dd186.unifood.Adapters.CheckoutProductAdapter;
import dd186.unifood.Entities.Category;
import dd186.unifood.Entities.Deal;
import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Order;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Main;
import dd186.unifood.R;

public class BasketFragment extends Fragment {
    TextView badge;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_basket, null );
        Main main = (Main) getActivity();
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
        if (!Main.basket.isEmpty()) {
            listView.setAdapter(new CheckoutProductAdapter(main,  Main.basket, listView,payByCard,payByCash,total,empty,badge,discount,finalTotal,tableLayout, this));
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Fragment productView = new ProductInfoFragment();
                Bundle args1 = new Bundle();
                args1.putSerializable("product", (Serializable) Main.products.get(position));
                productView.setArguments(args1);
                main.loadFragment(productView,"productInfo");
            });
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
        } else {
            listView.setVisibility(View.INVISIBLE);
            payByCard.setVisibility(View.INVISIBLE);
            payByCash.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
        }
        if (Main.pendingOrder != null){
            payByCard.setClickable(false);
            payByCash.setClickable(false);
        }

        return rootView;
    }

    //method used to calculate the total price of the products in the basket
    public double findTotal(){
        double totalPrice = 0;
        for (Product p : Main.basket) {
            totalPrice += (p.getPrice()* p.getQuantityInBasket()) ;
        }
        return totalPrice;
    }

    //method used to calculate the discount
    public double findDiscount(){
        // temporary list used to test if any of the offers or the deals apply without modifying the original list(products)
        List<Product> tester = new ArrayList<>();
        for (Product p :Main.basket) {
            for (int i = 0; i<p.getQuantityInBasket(); i++){
                tester.add(p);
            }
        }
        double discountValue=0;
        for (Offer o:Main.offers) {
            //checks whether the offer is in the basket
            if (o.getQuantityInBasket()>0){
                double priceBeforeDiscount = 0;
                for (Product p:o.getProductsInOffer()) {
                    tester.remove(p);
                    priceBeforeDiscount += p.getPrice();
                }
                double diff = priceBeforeDiscount - o.getValue();
                discountValue += diff;
            }
        }

        for (Deal d:Main.deals) {
            //list used to store products temporarily just to see if the deal applies
            List<Product> temp = new ArrayList<>();
            //the number of the products in the deal
            int numOfProductsInDeal = 0;
            for (Category c:d.getCategoriesInDeal()) {
                for (int i= 0; i<c.getQuantityInDeal(); i++) {
                    for (Product p : tester) {
                        //checks whether the product's category is the one im looking for
                        if (p.getCategory().getId() == c.getId()) {
                            temp.add(p);
                            tester.remove(p);
                            break;
                        }
                    }
                }
                //add how many products for the specific category
                numOfProductsInDeal += c.getQuantityInDeal();
            }
            //checks whether the temp list has enough products
            if (temp.size() == numOfProductsInDeal){
                double originalPrice = 0 ;
                for (Product p:temp) {
                    originalPrice += p.getPrice();
                }
                double diff = originalPrice - d.getValue();
                discountValue += diff;
            } else {
                //reset tester because the deal does not applies
                tester.addAll(temp);
            }
        }
        return discountValue;
    }




}
