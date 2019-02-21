package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import dd186.unifood.Adapters.CheckoutProductAdapter;
import dd186.unifood.Adapters.ProductAdapter;
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
        User user =  main.getUser();
        badge = main.getBasketNumTextView();
        List<Product> products = main.getBasketProducts();
        ListView listView = rootView.findViewById(R.id.basket_list);
        Button payByCard = rootView.findViewById(R.id.card_checkout_btn);
        Button payByCash = rootView.findViewById(R.id.cash_checkout_btn);
        TextView total = rootView.findViewById(R.id.total_basket);
        TextView totalHeader =  rootView.findViewById(R.id.total_header);
        TextView empty = rootView.findViewById(R.id.empty_basket);
        if (!products.isEmpty()) {
            listView.setAdapter(new CheckoutProductAdapter(products, listView,payByCard,payByCash,total,totalHeader,empty,badge));
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Fragment productView = new ProductInfoFragment();
                Bundle args1 = new Bundle();
                args1.putSerializable("product", (Serializable) products.get(position));
                productView.setArguments(args1);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, productView, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            });
            double totalPrice = 0;
            for (Product p : products) {
                totalPrice += (p.getPrice()* p.getQuantityInBasket()) ;
            }
            NumberFormat formatter = new DecimalFormat("#0.00");
            total.setText(formatter.format(totalPrice));
            empty.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.INVISIBLE);
            payByCard.setVisibility(View.INVISIBLE);
            payByCash.setVisibility(View.INVISIBLE);
            total.setVisibility(View.INVISIBLE);
            totalHeader.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }


}
