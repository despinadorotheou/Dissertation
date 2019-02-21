package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dd186.unifood.Adapters.DealsAdapter;
import dd186.unifood.Adapters.OfferAdapter;
import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Main;
import dd186.unifood.R;

public class OffersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_offers, null );
        Bundle args = getArguments();
        Main main = (Main) getActivity();
        assert main != null;
        RecyclerView recyclerView ;
        recyclerView =  rootView.findViewById(R.id.offer_list);
        assert args != null;
        List<Offer> offers = main.getOffers();
        recyclerView.addItemDecoration(new DividerItemDecoration(main, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(main, LinearLayoutManager.HORIZONTAL, false));
        // remove the offer if any of its items is out of stock
        List<Offer> availableOffers = new ArrayList<>();
        for (Offer o:offers) {
            boolean outOfStock = false;
            for (Product p:o.getProductsInOffer()){
                if (p.getQuantity() <= 0){
                    outOfStock=true;
                    break;
                }
            }
            if (!outOfStock){
                availableOffers.add(o);
            }
        }
        recyclerView.setAdapter(new OfferAdapter(main, availableOffers));
        return rootView;
    }
}
