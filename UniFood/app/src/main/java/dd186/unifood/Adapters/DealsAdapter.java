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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dd186.unifood.Entities.Deal;
import dd186.unifood.R;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder>{

    private List<Deal> dealList;
    private Context context;

    public DealsAdapter(List<Deal> dealList, Context context){
        this.dealList = dealList;
        this.context = context;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_item, parent, false);
        DealViewHolder dealViewHolder = new DealViewHolder(view);
        return dealViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, final int position) {
        if (dealList.get(position).getImage() != null){
            byte[] img = Base64.decode(dealList.get(position).getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0,img.length);
            holder.imageView.setImageBitmap(bitmap);

        } else {
            holder.imageView.setImageResource(R.drawable.ic_empty_image);
        }
        holder.txtview.setText(dealList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    class DealViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        DealViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.dealImg);
            txtview=view.findViewById(R.id.dealDescription);
        }
    }
}
