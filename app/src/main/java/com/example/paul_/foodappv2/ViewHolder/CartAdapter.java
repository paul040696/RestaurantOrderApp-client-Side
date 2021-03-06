package com.example.paul_.foodappv2.ViewHolder;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.paul_.foodappv2.Cart;
import com.example.paul_.foodappv2.Database.Database;
import com.example.paul_.foodappv2.Interface.ItemClickListener;
import com.example.paul_.foodappv2.Model.Order;
import com.example.paul_.foodappv2.R;

import java.util.ArrayList;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txt_cart_name,txt_price;
    public ElegantNumberButton btn_quantity;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView)
    {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price =(TextView)itemView.findViewById(R.id.cart_item_Price);
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);


    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {


   private List<Order> listData = new ArrayList<>();
   private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);




        //LayoutInflater este folosit sa creeze un View nou dintr-un xml Layout deja definit . Spre deosebire de findViewById , acesta creaza view-ul necesar in background ;
        //findViewById doar cauta o referinta catre un View deja creat

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {

       // TextDrawable drawable = TextDrawable.builder()
       //             .buildRound(""+listData.get(position).getQuantity(), Color.RED);
      //  holder.img_cart_count.setImageDrawable(drawable);

        //Butonul de cantitate pentru produse
        holder.btn_quantity.setNumber(listData.get(position).getQuantity());
        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order=listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //modificam totalul de plata in timp real
                int total = 0;
                List<Order> orders = new Database(cart).getCarts();
                for (Order item : orders)
                    total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(item.getQuantity()));
                Locale locale = new Locale("ro", "RO");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                cart.txtTotalPrice.setText(fmt.format(total));

            }
        });

        //Folosesc locale pentru a crea un obiect cu formatul datelor de tip RO (pt moneda RON);
        Locale locale = new Locale("ro","RO");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price =(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
