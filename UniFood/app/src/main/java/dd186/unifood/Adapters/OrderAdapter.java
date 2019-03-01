package dd186.unifood.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dd186.unifood.Entities.Offer;
import dd186.unifood.Entities.Order;
import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private List<Product> products;
    private Main main;

    public OrderAdapter(Main main, List<Order> orders, List<Product> products){
        this.main =main;
        this.orders = orders;
        this.products = products;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        holder.date.setText(orders.get(position).getDate());
        holder.id.setText(String.valueOf(orders.get(position).getId()));
        holder.value.setText("£"+formatter.format(orders.get(position).getValue()));
        holder.products.setAdapter(new ProductAdapterVertical(main, fromMapToList(orders.get(position).getProducts())) );

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private List<Product> fromMapToList(HashMap<String, Integer> map){
        List<Product> list = new ArrayList<>();
        for (String i:map.keySet()) {
            Product product = null;
            for (Product p:products) {
                if (p.getId() == Integer.parseInt(i)){
                    product = p;
                    break;
                }
            }
            if (product != null) {
                product.setQuantityInBasket(map.get(i));
                list.add(product);
            }
        }
        return list;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView date;
        TextView value;
        TextView status;
        ListView products;
        OrderViewHolder(View view) {
            super(view);
            id=view.findViewById(R.id.order_id_txt);
            date=view.findViewById(R.id.order_date);
            value = view.findViewById(R.id.order_value);
            status = view.findViewById(R.id.order_status);
            products = view.findViewById(R.id.order_list_products);
        }
    }
}
