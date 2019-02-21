package dd186.unifood.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder>{

    private List<Offer> offers;
    private Main main;

    public OfferAdapter(Main main, List<Offer> offers){
        this.main =main;
        this.offers = offers;
    }


    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        OfferViewHolder offerViewHolder = new OfferViewHolder(view);
        return offerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, final int position) {
        byte[] img = Base64.decode(offers.get(position).getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
        holder.imageView.setImageBitmap(bitmap);
        holder.txtview.setText(offers.get(position).getDescription());
        holder.button.setOnClickListener(v -> {
            main.addOfferToBasket(offers.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        Button button;
        OfferViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.offer_img);
            txtview=view.findViewById(R.id.offer_description);
            button = view.findViewById(R.id.add_basket_btn_offer);
        }
    }



//            boolean outOfStock = false;
//            for (Product p:offer.getProductsInOffer()){
//                if (p.getQuantity() <= 0){
//                    outOfStock = true;
//                    break;
//                }
//            }
//            if (outOfStock){
//                view.setVisibility(View.GONE);
//            }


}
