package dd186.unifood.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Main;
import dd186.unifood.R;

public class CategoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_category, null );
        GridView gridView;
        gridView = (GridView) rootView.findViewById(R.id.product_list);
        Bundle args = getArguments();
        Main main = (Main) getActivity();
        assert main != null;
        User user =  main.getUser();
        List<Product> products = (List<Product>) args.getSerializable("products");
        gridView.setAdapter(new ProductAdapter(main,products, user));
        gridView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(main, "click on item"+position,Toast.LENGTH_SHORT).show());
        return rootView;
    }
}
