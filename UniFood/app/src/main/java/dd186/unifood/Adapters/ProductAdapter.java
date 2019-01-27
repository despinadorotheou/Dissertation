package dd186.unifood.Adapters;

import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.sql.SQLException;
import java.util.*;

import dd186.unifood.Entities.Product;
import dd186.unifood.R;

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
//            ImageView image = new ImageView(context);
//            image.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_white_24dp));
//            view.addView(image);
            try {
                ImageView image = new ImageView(context);
                byte[] img = products.get(position).getImage().getBytes(1, (int) products.get(position).getImage().length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
                image.setImageBitmap(bitmap);
                view.addView(image);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            TextView nameTextView = new TextView(context);
            nameTextView.setText(products.get(position).getName());
            nameTextView.setPadding(0, 0, 10, 0);
            view.addView(nameTextView);
            TextView priceTextView = new TextView(context);
            priceTextView.setText("£" + Double.toString(products.get(position).getPrice()));
            view.addView(priceTextView);
            return view;
        }
        return convertView;

    }




}
