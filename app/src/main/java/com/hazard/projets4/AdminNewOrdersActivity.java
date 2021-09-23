package com.hazard.projets4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hazard.projets4.Model.AdminOrders;

import org.jetbrains.annotations.NotNull;

public class AdminNewOrdersActivity extends AppCompatActivity
{
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=
            new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull @NotNull AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull @NotNull AdminOrders adminOrders) {
                    adminOrdersViewHolder.username.setText("Name :"+adminOrders.getName());
                    adminOrdersViewHolder.userPhoneNumber.setText("Phone :"+adminOrders.getPhone());
                    adminOrdersViewHolder.userTotalPrice.setText("Total Amount :"+adminOrders.getTotalAmount());
                    adminOrdersViewHolder.userDateTime.setText("Order at :"+adminOrders.getDate());
                    adminOrdersViewHolder.userShippingAdress.setText("Shipping Adress :"+adminOrders.getAddress());

                    adminOrdersViewHolder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            String uid=getRef(i).getKey();

                            Intent intent=new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                            intent.putExtra("uid",uid);
                            startActivity(intent);

                        }
                    });

                    adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "Yes",
                                            "No"
                                    };
                            AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);
                            builder.setTitle("Have Shipped this order products");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    if (which==0)
                                    {
                                        String uid=getRef(i).getKey();

                                        removerOrder(uid);
                                    }
                                    else
                                    {
                                        finish();
                                    }

                                }
                            });
                            builder.show();
                        }
                    });

                }

                @NonNull
                @Override
                public AdminOrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                    return new AdminOrdersViewHolder(view);
                }
            };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removerOrder(String uid)
    {
        ordersRef.child(uid).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username, userPhoneNumber, userTotalPrice,userDateTime,userShippingAdress;
        public Button showOrdersBtn;

        public AdminOrdersViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            username =itemView.findViewById(R.id.order_user_name);
            userPhoneNumber =itemView.findViewById(R.id.order_phone_number);
            userTotalPrice =itemView.findViewById(R.id.order_total_price);
            userDateTime =itemView.findViewById(R.id.order_date_time);
            userShippingAdress =itemView.findViewById(R.id.order_adress_city);
            showOrdersBtn =itemView.findViewById(R.id.show_all_products);



        }
    }
}