package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Main;
import dd186.unifood.R;

public class FavouritesFragment extends Fragment {
    List<Product> favourites = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_favourites, null );
        GridView gridView;
        gridView = (GridView) rootView.findViewById(R.id.product_list);
        Main main = (Main) getActivity();
        assert main != null;
        favourites = main.getFavourites();
        User user =  main.getUser();
        gridView.setAdapter(new ProductAdapter(main,favourites, user));
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Fragment productView = new ProductInfoFragment();
            Bundle args =  new Bundle();
            args.putSerializable("product", (Serializable) favourites.get(position));
            productView.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, productView, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        });
        return rootView;

    }
}
