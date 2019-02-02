package dd186.unifood.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dd186.unifood.Entities.Product;
import dd186.unifood.R;

public class ProductInfoFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, null );
        Bundle args = getArguments();
        Product product = (Product) args.getSerializable("product");
        if (product!= null){
            ImageView image = view.findViewById(R.id.product_img);
            byte[] img = Base64.decode(product.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
            image.setImageBitmap(bitmap);
            TextView name = view.findViewById(R.id.product_name);
            name.setText(product.getName());
            TextView price = view.findViewById(R.id.product_price);
            price.setText("£"+ Double.toString(product.getPrice()));
            TextView description = view.findViewById(R.id.product_descriptiom);
            description.setText(product.getDescription());
        }
        return view;
    }
}
