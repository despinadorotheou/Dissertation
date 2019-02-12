package dd186.unifood.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

public class CheckoutProductAdapter  extends BaseAdapter {

    private List<Product> products;
    private ListView listView;
    private Button payByCard;
    private Button payByCash;
    private TextView total;
    private TextView totalHeader;
    private TextView empty;
    private TextView badge;

    public CheckoutProductAdapter(List<Product> products, ListView listView, Button payByCard, Button payByCash,TextView total, TextView totalHeader, TextView empty, TextView badge) {
        this.products = products;
        this.listView= listView;
        this.payByCard = payByCard;
        this.payByCash =  payByCash;
        this.empty =  empty;
        this.total =  total;
        this.totalHeader= totalHeader;
        this.badge = badge;
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
            Main main = new Main();
            Product product = products.get(position);
            Context context = parent.getContext();
            LinearLayout view =  new LinearLayout(context);
            view.setOrientation(LinearLayout.HORIZONTAL);
            ImageView image = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150,150);
            image.setLayoutParams(params);
            byte[] img = Base64.decode(product.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
            image.setImageBitmap(bitmap);
            view.addView(image);
            LinearLayout view2 =  new LinearLayout(context);
            view2.setOrientation(LinearLayout.VERTICAL);
            view2.setLayoutParams(new ViewGroup.LayoutParams(500,150));
            TextView nameTextView = new TextView(context);
            nameTextView.setText(product.getName());
            nameTextView.setPadding(0, 0, 0, 0);
            view2.addView(nameTextView);
            TextView quantityTextView = new TextView(context);
            quantityTextView.setText("Quantity: "+Integer.toString(product.getQuantityInBasket()));
            quantityTextView.setPadding(0, 0, 0, 0);
            view2.addView(quantityTextView);
            TextView priceTextView = new TextView(context);
            priceTextView.setText("£" + Double.toString(product.getPrice()));
            priceTextView.setPadding(0,0,0,30);
            view2.addView(priceTextView);
            view.addView(view2);
            ImageView removeIcon = new ImageView(context);
            removeIcon.setBackgroundResource(R.drawable.ic_clear);
            removeIcon.setLayoutParams(new ViewGroup.LayoutParams(40,40));

            removeIcon.setOnClickListener(v -> {
                products.remove(product);
                product.setQuantity(product.getQuantity()+product.getQuantityInBasket());
                product.setQuantityInBasket(0);
                main.removeFromBasket(products, badge);
                if (products.isEmpty()){
                    listView.setVisibility(View.INVISIBLE);
                    payByCard.setVisibility(View.INVISIBLE);
                    payByCash.setVisibility(View.INVISIBLE);
                    total.setVisibility(View.INVISIBLE);
                    totalHeader.setVisibility(View.INVISIBLE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    double updateTotal = 0;
                    for (Product p : products) {
                        updateTotal += (p.getPrice() * p.getQuantityInBasket());
                    }
                    total.setText(Double.toString(updateTotal));
                }
                notifyDataSetChanged();

            });
            view.addView(removeIcon);
            return view;
        }
        return convertView;

    }
}
