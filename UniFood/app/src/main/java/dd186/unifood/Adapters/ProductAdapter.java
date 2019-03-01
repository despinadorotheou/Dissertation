package dd186.unifood.Adapters;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
//import org.apache.commons.codec.binary.Base64;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Fragments.ProductInfoFragment;
import dd186.unifood.HttpGetRequest;
import dd186.unifood.Main;
import dd186.unifood.R;

public class ProductAdapter extends BaseAdapter {

    private List<Product> products;
    private User user;
    private Main main;

    public ProductAdapter(Main main, List<Product> products, User user){
        this.main =main;
        this.products = products;
        this.user = user;
    }

    @Override
    public int getCount(){
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Product product = getItem(position);
            Context context = parent.getContext();
            GridLayout view = new GridLayout(context);
            view.setOrientation(GridLayout.VERTICAL);
            view.setUseDefaultMargins(true);
            view.setLayoutParams(new ViewGroup.LayoutParams(250,450));
            view.setAlignmentMode(GridLayout.ALIGN_MARGINS);
            ImageView image = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150,150);
            image.setLayoutParams(params);
            byte[] img = Base64.decode(product.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
            image.setImageBitmap(bitmap);
            view.addView(image);
            ImageView favouriteIcon = new ImageView(context);
            boolean productInFavourites = false;
            for (Product p: user.getFavouriteProducts()) {
                if (p.getId()==product.getId()){
                    productInFavourites = true;
                }
            }
            if(productInFavourites){
                favouriteIcon.setBackgroundResource(R.drawable.ic_favorite);
                favouriteIcon.setLayoutParams(new ViewGroup.LayoutParams(30,30));
                favouriteIcon.setOnClickListener(v -> {
                    user.getFavouriteProducts().remove(product);
                    HttpGetRequest httpGetRequest = new HttpGetRequest();
                    httpGetRequest.setLink("http://10.0.2.2:8080/rest/removeFavourite/" + user.getId() +"/" + product.getId());
                    httpGetRequest.execute();
                    favouriteIcon.setBackgroundResource(R.drawable.ic_favorite_border);
                    main.setUser(user);
                    notifyDataSetChanged();


                });
            }else{
                favouriteIcon.setBackgroundResource(R.drawable.ic_favorite_border);
                favouriteIcon.setLayoutParams(new ViewGroup.LayoutParams(30,30));
                favouriteIcon.setOnClickListener(v -> {
                    user.getFavouriteProducts().add(product);
                    HttpGetRequest httpGetRequest = new HttpGetRequest();
                    httpGetRequest.setLink("http://10.0.2.2:8080/rest/addFavourite/" + user.getId() +"/" +product.getId());
                    httpGetRequest.execute();
                    favouriteIcon.setBackgroundResource(R.drawable.ic_favorite);
                    main.setUser(user);
                    notifyDataSetChanged();

                });
            }
            view.addView(favouriteIcon);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(product.getName());
            nameTextView.setPadding(0, 0, 0, 0);
            view.addView(nameTextView);
            TextView priceTextView = new TextView(context);
            NumberFormat formatter = new DecimalFormat("#0.00");
            priceTextView.setText("£" + formatter.format(product.getPrice()));
            priceTextView.setPadding(0,0,0,30);
            view.addView(priceTextView);
            if (product.getQuantity()<=0){
                view.setAlpha(0.4f);
                priceTextView.setVisibility(View.GONE);
                favouriteIcon.setVisibility(View.GONE);
                TextView outOfStock = new TextView(context);
                outOfStock.setText(context.getString(R.string.out_of_stock_msg));
                view.addView(outOfStock);
            }
            else {
                view.setOnClickListener(v -> {
                    Fragment productView = new ProductInfoFragment();
                    Bundle args =  new Bundle();
                    args.putSerializable("product", (Serializable) product);
                    productView.setArguments(args);
                    main.loadFragment(productView);
                });
            }
            return view;
        }
        return convertView;

    }


}
