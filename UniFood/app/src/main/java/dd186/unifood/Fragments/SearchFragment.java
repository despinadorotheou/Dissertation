package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    EditText searchInput;
    GridView gridView;
    Main main;
    List<Product> beforeFilter = new ArrayList<>();
    int maxFilter = 10;
    boolean veganFilterApplied = false;
    boolean vegetarianFilterApplied = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container,false );
        gridView = (GridView) rootView.findViewById(R.id.product_list);
        main = (Main) getActivity();
        assert main != null;
        Bundle args = getArguments();
        assert args != null;
        products = (List<Product>) args.getSerializable("products");
        currentDisplayedProducts = products;
        searchInput = rootView.findViewById(R.id.search_input);
        gridView.setAdapter(new ProductAdapter(main,products));
        Spinner searchFilter = rootView.findViewById(R.id.spinner_filter);
        String[] items = new String[]{"Name", "Ingredients"};
        ArrayAdapter<String> adapter =  new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),R.layout.spinner_item, items);
        searchFilter.setAdapter(adapter);
        searchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    searchByName();
                }else if (position==1){
                    searchByIngredients();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Switch vegan = rootView.findViewById(R.id.vegan_switch);
        Switch vegetarian = rootView.findViewById(R.id.vegetaterian_switch);
        vegan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //list used to store the products that was displayed on the screen before sleekbar changes
            List<Product> result = new ArrayList<>();
            if (isChecked) {
                veganFilterApplied = true;
                for (Product p : products) {
                    if (p.getPreference().equals("Vegan")&& p.getPrice()<=maxFilter) {
                        result.add(p);
                    }
                }
                vegetarian.setClickable(false);
            } else {
                veganFilterApplied = false;
                for (Product p : products) {
                    if (p.getPrice()<=maxFilter) {
                        result.add(p);
                    }
                }
                vegetarian.setClickable(true);
            }
            currentDisplayedProducts = result;
            gridView.setAdapter(new ProductAdapter(main, result));
        });
        vegetarian.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //list used to store the products that was displayed on the screen before sleekbar changes
            List<Product> result = new ArrayList<>();
            if (isChecked) {
                vegetarianFilterApplied = true;
                for (Product p : products) {
                    if ((p.getPreference().equals("Vegetarian") || p.getPreference().equals("Vegan"))&& p.getPrice()<=maxFilter) {
                        result.add(p);
                    }
                }
                vegan.setClickable(false);
            } else {
                vegetarianFilterApplied = false;
                vegan.setClickable(true);
                for (Product p : products) {
                    if (p.getPrice()<=maxFilter) {
                        result.add(p);
                    }
                }
            }
            currentDisplayedProducts = result;
            gridView.setAdapter(new ProductAdapter(main, result));
        });
        SeekBar costFilter = rootView.findViewById(R.id.cost_constain);
        TextView maxCost = rootView.findViewById(R.id.max_txt_search);
        beforeFilter = currentDisplayedProducts;
        costFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //list used to store the products that was displayed on the screen before sleekbar changes
            int progressNum = 10;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progressNum = progress;
                maxCost.setText("£"+progressNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                List<Product> result = new ArrayList<>();
                for (Product p : products) {
                    if (veganFilterApplied) {
                        if (p.getPrice()<=progressNum && p.getPreference().equals("Vegan")) {
                            result.add(p);
                        }
                    } else if (vegetarianFilterApplied) {
                        if (p.getPrice()<=progressNum && (p.getPreference().equals("Vegan") || p.getPreference().equals("Vegetarian"))) {
                            result.add(p);
                        }
                    } else {
                        if (p.getPrice()<=progressNum){
                            result.add(p);
                        }
                    }

                }
                currentDisplayedProducts = result;
                gridView.setAdapter(new ProductAdapter(main, result));
                maxFilter= progressNum;
            }
        });
        return rootView;
    }

    private void searchByName(){
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                List<Product> result = new ArrayList<>();
                for (Product p : currentDisplayedProducts) {
                    if (Pattern.compile(Pattern.quote(cs.toString()), Pattern.CASE_INSENSITIVE).matcher(p.getName()).find()) {
                        result.add(p);
                    }
                }
                gridView.setAdapter(new ProductAdapter(main, result));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
    }

    private void searchByIngredients(){
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                List<Product> result = new ArrayList<>();
                for (Product p : currentDisplayedProducts) {
                    if (Pattern.compile(Pattern.quote(cs.toString()), Pattern.CASE_INSENSITIVE).matcher(p.getIngredients()).find()) {
                        result.add(p);
                    }
                }
                gridView.setAdapter(new ProductAdapter(main, result));

            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
    }


}
