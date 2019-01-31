package dd186.unifood.Adapters;

import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.sql.SQLException;
import java.util.*;
//import org.apache.commons.codec.binary.Base64;
import dd186.unifood.Entities.Product;

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
            view.setUseDefaultMargins(true);
            view.setAlignmentMode(GridLayout.ALIGN_MARGINS);
            ImageView image = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150,150);
            image.setLayoutParams(params);
            byte[] img = Base64.decode(products.get(position).getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
            image.setImageBitmap(bitmap);
            view.addView(image);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(products.get(position).getName());
            nameTextView.setPadding(0, 0, 0, 0);
            view.addView(nameTextView);
            TextView priceTextView = new TextView(context);
            priceTextView.setText("£" + Double.toString(products.get(position).getPrice()));
            priceTextView.setPadding(0,0,0,30);
            view.addView(priceTextView);
            return view;
        }
        return convertView;

    }




}
