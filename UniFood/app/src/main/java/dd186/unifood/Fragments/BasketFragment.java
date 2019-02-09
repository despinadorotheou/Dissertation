package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import dd186.unifood.Adapters.CheckoutProductAdapter;
import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Main;
import dd186.unifood.R;

public class BasketFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_basket, null );
        Main main = (Main) getActivity();
        assert main != null;
        User user =  main.getUser();
        List<Product> products = main.getBasketProducts();
        ListView listView = rootView.findViewById(R.id.basket_list);
        listView.setAdapter(new CheckoutProductAdapter(main,products));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Fragment productView = new ProductInfoFragment();
            Bundle args1 =  new Bundle();
            args1.putSerializable("product", (Serializable) products.get(position));
            productView.setArguments(args1);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, productView, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        });        return rootView;
    }
}
