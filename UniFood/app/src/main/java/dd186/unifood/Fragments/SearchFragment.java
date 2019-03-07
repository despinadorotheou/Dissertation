package dd186.unifood.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dd186.unifood.Adapters.ProductAdapter;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Main;
import dd186.unifood.R;

public class SearchFragment extends Fragment {

    List<Product> products = new ArrayList<>();
    List<Product> currentDisplayedProducts;
    User user ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container,false );
        GridView gridView;
        gridView = (GridView) rootView.findViewById(R.id.product_list);
        Main main = (Main) getActivity();
        assert main != null;
        Bundle args = getArguments();
        assert args != null;
        products = (List<Product>) args.getSerializable("products");
        user = main.getUser();
        currentDisplayedProducts = products;
        EditText searchInput = rootView.findViewById(R.id.search_input);
        gridView.setAdapter(new ProductAdapter(main,products, user));
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                List<Product> result = new ArrayList<>();
                for (Product p:currentDisplayedProducts) {
                    if (Pattern.compile(Pattern.quote(cs.toString()), Pattern.CASE_INSENSITIVE).matcher(p.getName()).find()){
                        result.add(p);
                    }
                }
                gridView.setAdapter(new ProductAdapter(main,result, user));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
        Switch vegan = rootView.findViewById(R.id.vegan_switch);
        Switch vegetarian = rootView.findViewById(R.id.vegetaterian_switch);
        vegan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                List<Product> result = new ArrayList<>();
                for (Product p : currentDisplayedProducts) {
                    if (p.getPreference().equals("Vegan")) {
                        result.add(p);
                    }
                }
                currentDisplayedProducts = result;
                gridView.setAdapter(new ProductAdapter(main, result, user));
                vegetarian.setClickable(false);
            } else {
                currentDisplayedProducts = products;
                gridView.setAdapter(new ProductAdapter(main,products, user));
                vegetarian.setClickable(true);

            }
        });
        vegetarian.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                List<Product> result = new ArrayList<>();
                for (Product p : currentDisplayedProducts) {
                    if (p.getPreference().equals("Vegetarian") || p.getPreference().equals("Vegan")) {
                        result.add(p);
                    }
                }
                currentDisplayedProducts = result;
                gridView.setAdapter(new ProductAdapter(main, result, user));
                vegan.setClickable(false);
            } else {
                currentDisplayedProducts = products;
                gridView.setAdapter(new ProductAdapter(main,products, user));
                vegan.setClickable(true);

            }
        });
        return rootView;
    }


}
