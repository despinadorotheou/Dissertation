package Adapters;

import android.app.Activity;
import android.content.*;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

import Entities.Product;
import example.unifood.R;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> products;
    private Resources resources;
    private static LayoutInflater inflater = null;

    public ProductAdapter(Context context, List<Product> products, Resources resources){
       this.context= context;
       this.products = products;
       this.resources = resources;
       inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            Context context = parent.getContext();
            GridLayout view = new GridLayout(context);
            view.setOrientation(GridLayout.VERTICAL);
            ImageView image = new ImageView(context);
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_white_24dp));
            view.addView(image);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(products.get(position).getName());
            nameTextView.setPadding(0, 0, 10, 0);
            view.addView(nameTextView);
            TextView priceTextView = new TextView(context);
            priceTextView.setText("£" + Double.toString(products.get(position).getPrice()));
            view.addView(priceTextView);
            return view;
//            View itemView = convertView;
//            Product selectedProduct = products.get(position);
//            itemView = (itemView == null) ? inflater.inflate(R.layout.product_grid_layout, null) : itemView;
////        TextView textViewName = (TextView) itemView.findViewById(R.id.textViewName);
//        String name = selectedProduct.getName();
//        textViewName.setText(name);

//            return itemView;
        }
        return convertView;

    }




}
