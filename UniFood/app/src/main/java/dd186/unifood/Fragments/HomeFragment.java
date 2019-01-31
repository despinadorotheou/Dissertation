package dd186.unifood.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

public class HomeFragment extends Fragment {

    List<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container,false );
        GridView gridView;
        gridView = (GridView) rootView.findViewById(R.id.product_list);
        Main main = (Main) getActivity();
        assert main != null;
        products = main.getProducts();
        Resources resources = getResources();
        gridView.setAdapter(new ProductAdapter(main,products, resources));
//        gridView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(main, "click on item"+position,Toast.LENGTH_SHORT).show());
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Fragment productView = new ProductInfoFragment();
            Bundle args =  new Bundle();
            args.putSerializable("product", (Serializable) products.get(position));
            productView.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, productView, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        });
        return rootView;
    }


}
