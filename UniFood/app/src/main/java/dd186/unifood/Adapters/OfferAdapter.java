package dd186.unifood.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

public class OfferAdapter extends BaseAdapter {

    private List<Offer> offers;
    private Main main;

    public OfferAdapter(Main main, List<Offer> offers){
        this.main =main;
        this.offers = offers;
    }

    @Override
    public int getCount(){
        return offers.size();
    }

    @Override
    public Offer getItem(int position) {
        return offers.get(position);    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Offer offer = getItem(position);
            Context context = parent.getContext();
            GridLayout view = new GridLayout(context);
            view.setOrientation(GridLayout.VERTICAL);
            view.setUseDefaultMargins(true);
            view.setLayoutParams(new ViewGroup.LayoutParams(250,450));
            view.setAlignmentMode(GridLayout.ALIGN_MARGINS);
            ImageView image = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150,150);
            image.setLayoutParams(params);
            byte[] img = Base64.decode(offer.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
            image.setImageBitmap(bitmap);
            view.addView(image);
            TextView nameTextView = new TextView(context);
            nameTextView.setText(offer.getDescription());
            nameTextView.setPadding(0, 0, 0, 0);
            view.addView(nameTextView);
            TextView priceTextView = new TextView(context);
            NumberFormat formatter = new DecimalFormat("#0.00");
            priceTextView.setText("£" + formatter.format(offer.getValue()));
            priceTextView.setPadding(0,0,0,30);
            view.addView(priceTextView);
            Button button = new Button(context);
            button.setText(context.getString(R.string.add_to_basket_btn));
            button.setOnClickListener(v -> {
                main.addOfferToBasket(offer);
            });
            boolean outOfStock = false;
            for (Product p:offer.getProductsInOffer()){
                if (p.getQuantity() <= 0){
                    outOfStock = true;
                    break;
                }
            }
            if (outOfStock){
                view.setVisibility(View.GONE);
            }
            return view;
        }
        return convertView;

    }


}
