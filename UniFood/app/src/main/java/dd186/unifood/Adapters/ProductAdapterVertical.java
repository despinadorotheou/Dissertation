package dd186.unifood.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

public class ProductAdapterVertical extends BaseAdapter {

    private Main main;
    private List<Product> products;

    public ProductAdapterVertical(Main main, List<Product> products) {
        this.main = main;
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
            Product product = products.get(position);
            Context context = parent.getContext();
            LinearLayout view =  new LinearLayout(context);
            view.setOrientation(LinearLayout.HORIZONTAL);
            ImageView image = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100,100);
            image.setLayoutParams(params);
            if (product.getImage() != null){
                byte[] img = Base64.decode(product.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
                image.setImageBitmap(bitmap);

            } else {
                image.setImageResource(R.drawable.ic_empty_image);
            }
            view.addView(image);
            LinearLayout view2 =  new LinearLayout(context);
            view2.setOrientation(LinearLayout.VERTICAL);
            view2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,100));
            TextView nameTextView = new TextView(context);
            nameTextView.setText(product.getName());
            view2.addView(nameTextView);
            TextView quantityTextView = new TextView(context);
            quantityTextView.setText("Quantity: "+Integer.toString(product.getQuantityInBasket()));
            view2.addView(quantityTextView);
            view.addView(view2);
            return view;
        }
        return convertView;

    }
}
