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
import java.util.concurrent.ExecutionException;
//import org.apache.commons.codec.binary.Base64;
import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;
import dd186.unifood.Fragments.ProductInfoFragment;
import dd186.unifood.HttpGetRequest;
import dd186.unifood.Main;
import dd186.unifood.R;

import static dd186.unifood.Main.user;

public class ProductAdapter extends BaseAdapter {

    private List<Product> products;
    private Main main;

    public ProductAdapter(Main main, List<Product> products){
        this.main =main;
        this.products = products;
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
            for (Product p: Main.favourites) {
                if (p.getId()==product.getId()){
                    productInFavourites = true;
                }
            }
            if(productInFavourites){
                favouriteIcon.setBackgroundResource(R.drawable.ic_favorite);
                favouriteIcon.setLayoutParams(new ViewGroup.LayoutParams(30,30));
                favouriteIcon.setOnClickListener(v -> {
                    try {
                        main.makeHttpRequest("http://10.0.2.2:8080/rest/removeFavourite/" + Main.user.getId() +"/" + product.getId());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    favouriteIcon.setBackgroundResource(R.drawable.ic_favorite_border);
                    Main.favourites.remove(product);
                    notifyDataSetChanged();


                });
            }else{
                favouriteIcon.setBackgroundResource(R.drawable.ic_favorite_border);
                favouriteIcon.setLayoutParams(new ViewGroup.LayoutParams(30,30));
                favouriteIcon.setOnClickListener(v -> {
                    try {
                        main.makeHttpRequest("http://10.0.2.2:8080/rest/addFavourite/" + Main.user.getId() +"/" +product.getId());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    favouriteIcon.setBackgroundResource(R.drawable.ic_favorite);
                    Main.favourites.add(product);
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
                    main.loadFragment(productView, "productInfo");
                });
            }
            return view;
        }
        return convertView;

    }


}
