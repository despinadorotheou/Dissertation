package dd186.unifood.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

import static dd186.unifood.Main.basket;
import static dd186.unifood.Main.makeARequest;

public class ProductInfoFragment extends Fragment {

    int qInt =1;
    TextView quantity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, null );
        Bundle args = getArguments();
        Product product = (Product) args.getSerializable("product");
        if (product!= null){
            Main main = (Main) getActivity();
            assert main != null;
            resetQuantityInt();
            ImageView image = view.findViewById(R.id.product_img);
            if (product.getImage() != null){
                byte[] img = Base64.decode(product.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
                image.setImageBitmap(bitmap);

            } else {
                image.setImageResource(R.drawable.ic_empty_image);
            }
            TextView name = view.findViewById(R.id.product_name);
            name.setText(product.getName());
            NumberFormat formatter = new DecimalFormat("#0.00");
            TextView price = view.findViewById(R.id.product_price);
            price.setText("£"+ formatter.format(product.getPrice()));
            TextView description = view.findViewById(R.id.product_descriptiom);
            description.setText(product.getDescription());
            ConstraintLayout vegan = view.findViewById(R.id.vegan_layout);
            ConstraintLayout vegetarian = view.findViewById(R.id.vegetarian_layout);
            if (product.getPreference() != null)
                switch (product.getPreference()) {
                    case "Vegan":
                        vegetarian.setVisibility(View.GONE);
                        break;
                    case "Vegetarian":
                        vegan.setVisibility(View.GONE);
                        break;
                    default:
                        vegetarian.setVisibility(View.GONE);
                        vegan.setVisibility(View.GONE);
                        break;
                }
            quantity = view.findViewById(R.id.amount_quantity);
            Button addToBasket = view.findViewById(R.id.add_basket_btn);
            addToBasket.setOnClickListener(v -> {
                main.refreshProducts();
                if(main.checkProduct(product).getQuantity()>=1) {
                    int quantityInBasket = Integer.parseInt(quantity.getText().toString());
                    product.setQuantity(product.getQuantity() - quantityInBasket);
                    makeARequest.get("http://10.0.2.2:8080/rest/decreaseQuantity/" + product.getId() + "/" + quantityInBasket);
                    boolean existsInBasket = false;
                    for (Product p : basket) {
                        if (p.getId() == product.getId()) {
                            int previousQuantity = p.getQuantityInBasket();
                            p.setQuantityInBasket(previousQuantity + quantityInBasket);
                            existsInBasket = true;
                        }
                    }
                    if (!existsInBasket) {
                        product.setQuantityInBasket(quantityInBasket);
                        basket.add(product);
                    }
                    main.countBasketBadge();
                    Toast.makeText(main.getApplicationContext(), "Added to the basket!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(main.getApplicationContext(), "Sorry item out of stock!", Toast.LENGTH_SHORT).show();

                }
                Fragment reload = new HomeFragment();
                main.loadFragment(reload, "home");
                });

            Button increaseQ = view.findViewById(R.id.increase_quantity_btn);
            increaseQ.setOnClickListener(v -> {
                if(product.getQuantity()>= qInt +1) {
                    qInt++;}
                display(qInt);
            });

            Button decreaseQ = view.findViewById(R.id.decrease_quantity_btn);
            decreaseQ.setOnClickListener(v -> {
                if (qInt > 1)
                    qInt = qInt - 1;
                display(qInt);
            });


        }
        return view;
    }

    private void display(int number) {
        quantity.setText("" + number);
    }

    public void resetQuantityInt(){
        qInt = 1;
    }



}
