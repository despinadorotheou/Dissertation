package dd186.unifood.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;

import dd186.unifood.Entities.Order;
import dd186.unifood.Entities.Product;
import dd186.unifood.Main;
import dd186.unifood.R;

import static dd186.unifood.Main.basket;
import static dd186.unifood.Main.makeARequest;
import static dd186.unifood.Main.pendingOrder;
import static dd186.unifood.Main.user;

public class CardInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_card_info, null );
        Main main = (Main) getActivity();
        assert main != null;
        Bundle args = getArguments();
        assert args != null;
        double total = args.getDouble("total");
        TextView finalTotal = rootView.findViewById(R.id.amount_card_txt);
        finalTotal.setText(String.valueOf(total));
        Button confirmCardDetails = rootView.findViewById(R.id.confirm_btn_card);
        confirmCardDetails.setOnClickListener(v -> {
            HashMap<String,String> id_quanity = new HashMap<>();
            for (Product p:basket) {
                id_quanity.put(String.valueOf(p.getId()), String.valueOf(p.getQuantityInBasket()));
            }
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson =  gsonBuilder.create();
            String json = gson.toJson(id_quanity);
            ObjectMapper objectMapper = new ObjectMapper();
            String response;
            if (!(response=makeARequest.post("http://10.0.2.2:8080/rest/addOrder/card/"+user.getId() + "/"+total, json)).isEmpty()){
                try {
                    pendingOrder = objectMapper.readValue(response, new TypeReference<Order>(){});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            main.afterPlaceOrder();
        });
        return rootView;

    }
}
