package dd186.unifood.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Product;
import dd186.unifood.Fragments.BasketFragment;
import dd186.unifood.Main;
import dd186.unifood.R;

public class CheckoutProductAdapter  extends BaseAdapter {

    private Main main;
    private List<Product> products;
    private ListView listView;
    private Button payByCard;
    private Button payByCash;
    private TextView total;
    private TextView empty;
    private TextView badge;
    private TextView finalTotal;
    private TextView discount;
    private TableLayout tableLayout;
    private BasketFragment basketFragment;

    public CheckoutProductAdapter(Main main, List<Product> products, ListView listView, Button payByCard, Button payByCash, TextView total, TextView empty, TextView badge,
                                  TextView discount, TextView finalTotal, TableLayout tableLayout, BasketFragment fragment) {
        this.main = main;
        this.products = products;
        this.listView= listView;
        this.payByCard = payByCard;
        this.payByCash =  payByCash;
        this.empty =  empty;
        this.total =  total;
        this.badge = badge;
        this.discount =discount;
        this.finalTotal = finalTotal;
        this.tableLayout = tableLayout;
        this.basketFragment = fragment;
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
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150,150);
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
            NumberFormat formatter = new DecimalFormat("#0.00");
            priceTextView.setText("£" + formatter.format(product.getPrice()));
            priceTextView.setPadding(0,0,0,30);
            view2.addView(priceTextView);
            view.addView(view2);
            ImageView removeIcon = new ImageView(context);
            removeIcon.setBackgroundResource(R.drawable.ic_clear);
            removeIcon.setLayoutParams(new ViewGroup.LayoutParams(40,40));

            removeIcon.setOnClickListener(v -> {
                products.remove(product);
                notifyDataSetChanged();
                main.removeFromBasket(product, badge);
                if (products.isEmpty()){
                    listView.setVisibility(View.INVISIBLE);
                    payByCard.setVisibility(View.INVISIBLE);
                    payByCash.setVisibility(View.INVISIBLE);
                    tableLayout.setVisibility(View.INVISIBLE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    double totalBeforeDiscount = basketFragment.findTotal();
                    total.setText(formatter.format(totalBeforeDiscount));
                    double discountValue=basketFragment.findDiscount();
                    if (discountValue !=0 ){
                        discount.setText(formatter.format(discountValue));

                    }else {
                        discount.setText("   ---   ");
                    }
                    double totalWithDiscount = totalBeforeDiscount - discountValue;
                    finalTotal.setText(formatter.format(totalWithDiscount));
                    notifyDataSetChanged();
                }

            });
            view.addView(removeIcon);
            view.setOnClickListener(v -> main.productInfoPage(product));
            return view;
        }
        return convertView;

    }
}
