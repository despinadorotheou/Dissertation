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

import java.util.List;

import dd186.unifood.Entities.Product;

public class CheckoutProductAdapter  extends BaseAdapter {

    private Context context;
    private List<Product> products;

    public CheckoutProductAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
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
            LinearLayout view =  new LinearLayout(context);
            view.setOrientation(LinearLayout.HORIZONTAL);
            ImageView image = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50,50);
            image.setLayoutParams(params);
            byte[] img = Base64.decode(products.get(position).getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
            image.setImageBitmap(bitmap);
            view.addView(image);
            LinearLayout view2 =  new LinearLayout(context);
            view2.setOrientation(LinearLayout.VERTICAL);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(products.get(position).getName());
            nameTextView.setPadding(0, 0, 0, 0);
            view2.addView(nameTextView);
            TextView priceTextView = new TextView(context);
            priceTextView.setText("£" + Double.toString(products.get(position).getPrice()));
            priceTextView.setPadding(0,0,0,30);
            view2.addView(priceTextView);
            view.addView(view2);
            return view;
        }
        return convertView;

    }
}
