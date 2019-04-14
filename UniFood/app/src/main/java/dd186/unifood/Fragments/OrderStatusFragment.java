package dd186.unifood.Fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import dd186.unifood.Adapters.ProductAdapterVertical;
import dd186.unifood.Entities.Order;
import dd186.unifood.Main;
import dd186.unifood.R;

public class OrderStatusFragment extends Fragment {

    public static int isVisible = View.VISIBLE;
    public static String currentStatus = "Pending...";
    public static Timer t;

    Main main;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_status, null );
        main = (Main) getActivity();
        assert main != null;
        ListView orderProducts = view.findViewById(R.id.pending_order_list);
        orderProducts.setAdapter(new ProductAdapterVertical(main,main.fromMapToList(Main.pendingOrder.getProducts())));
        TextView orderId = view.findViewById(R.id.order_num);
        orderId.setText("Order="+ Main.pendingOrder.getId());
        TextView status = view.findViewById(R.id.order_status_txt);
        status.setText(currentStatus);
        LinearLayout edit_delete = view.findViewById(R.id.edit_delete_layout);
        edit_delete.setVisibility(isVisible);
        Bundle args = getArguments();
        int firstTime = args.getInt("firstTime");
        if (firstTime == 1){
            //start a 5 minutes timer
            setTimer(edit_delete);
            // start a timer that checks the status of an order every 10seconds
            checkStatus(status);
        }
        return view;
    }

    public void setTimer(LinearLayout edit_delete){
        //5 mins
        CountDownTimer timer = new CountDownTimer(300000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (currentStatus.equals("Ready for collection!")){
                    OrderStatusFragment.isVisible = View.GONE;
                    edit_delete.setVisibility(View.GONE); //(or GONE)
                }
            }

            @Override
            public void onFinish() {
                OrderStatusFragment.isVisible = View.GONE;
                edit_delete.setVisibility(View.GONE); //(or GONE)
            }
        }.start();

    }

    public void checkStatus(TextView status){
        t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                main.runOnUiThread(() -> {
                    try {
                        OrderStatusFragment.currentStatus = main.makeHttpRequest("http://10.0.2.2:8080/rest/checkStatus/" +Main.pendingOrder.getId());
                        status.setText(currentStatus);
                        if (currentStatus.equals("Collected!")){
                            t.cancel();

                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 10000);
    }
}
