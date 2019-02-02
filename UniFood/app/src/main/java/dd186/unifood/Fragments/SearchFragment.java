package dd186.unifood.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

public class SearchFragment extends Fragment {

    List<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container,false );
        GridView gridView;
        gridView = (GridView) rootView.findViewById(R.id.product_list);
        Main main = (Main) getActivity();
        assert main != null;
        products = main.getProducts();
        Resources resources = getResources();
        EditText searchInput = rootView.findViewById(R.id.search_input);
        gridView.setAdapter(new ProductAdapter(main,products, resources));
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

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                List<Product> result = new ArrayList<>();
                for (Product p:products) {
                    if (Pattern.compile(Pattern.quote(cs.toString()), Pattern.CASE_INSENSITIVE).matcher(p.getName()).find()){
                        result.add(p);
                    }
                }
                gridView.setAdapter(new ProductAdapter(main,result, resources));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
        return rootView;
    }


}
