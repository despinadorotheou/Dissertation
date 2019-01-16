package Adapters;

import android.app.Activity;
import android.content.*;
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
    private static LayoutInflater inflater = null;

    public ProductAdapter(Context context, List<Product> products){
       this.context= context;
       this.products = products;
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
        View itemView = convertView;
        Product selectedProduct = products.get(position);
        itemView = (itemView == null) ? inflater.inflate(R.layout.product_grid_layout,null): itemView;
        TextView textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        String name = selectedProduct.getName();
        textViewName.setText(name);

        return itemView;

    }



}
