package com.hazard.projets4.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hazard.projets4.Interface.ItemClickListner;
import com.hazard.projets4.Prevalent.Prevalent;
import com.hazard.projets4.R;

import org.jetbrains.annotations.NotNull;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;
    public ImageButton likeButton;
    public TextView likesdisplay;
    int likesCounter=0;
    public DatabaseReference likesref;



    public ProductViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }

    public void setLikeButtonStatus(final String postkey)
    {
        likeButton=(ImageButton) itemView.findViewById(R.id.like_btn);
        likesdisplay=(TextView)itemView.findViewById(R.id.likes_counter);
        likesref= FirebaseDatabase.getInstance().getReference("likes");
        String curerntUserId= Prevalent.currentOnlineUser.getPhone();
        String likes="likes";

        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
                if(snapshot.child(postkey).hasChild(curerntUserId))
                {
                    likesCounter=(int)snapshot.child(postkey).getChildrenCount();
                    likeButton.setImageResource(R.drawable.ic_like);
                    likesdisplay.setText(Integer.toString(likesCounter)+likes);
                }else
                    {
                        likesCounter=(int)snapshot.child(postkey).getChildrenCount();
                        likeButton.setImageResource(R.drawable.ic_unlike);
                        likesdisplay.setText(Integer.toString(likesCounter)+likes);

                    }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}

