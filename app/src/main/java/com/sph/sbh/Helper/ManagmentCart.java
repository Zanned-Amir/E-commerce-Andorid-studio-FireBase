package com.sph.sbh.Helper;

import android.content.Context;
import android.widget.Toast;


import com.sph.sbh.Model.ItemsDomain;

import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }
    public static int getCount(Context context) {
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<ItemsDomain> cartItems = tinyDB.getListObject("CartList");
        if (cartItems != null) {
            return cartItems.size();
        } else {
            return 0;
        }
    }

    public void insertItem(ItemsDomain item) {
        ArrayList<ItemsDomain> listCart = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listCart.size(); y++) {
            if (listCart.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listCart.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            listCart.add(item);
        }
        tinyDB.putListObject("CartList", listCart);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsDomain> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<ItemsDomain> listCart, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listCart.get(position).getNumberinCart() == 1) {
            listCart.remove(position);
        } else {
            listCart.get(position).setNumberinCart(listCart.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listCart);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemsDomain> listCart, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        int maxQuantity = 10; // Maximum quantity allowed
        int currentQuantity = listCart.get(position).getNumberinCart();

        if (currentQuantity < maxQuantity) {
            listCart.get(position).setNumberinCart(currentQuantity + 1);
            tinyDB.putListObject("CartList", listCart);
            changeNumberItemsListener.changed();
        } else {
            // Notify the user that the maximum quantity has been reached
            Toast.makeText(context, "Maximum quantity reached for this item", Toast.LENGTH_SHORT).show();
        }
    }


    public Double getTotalFee() {
        ArrayList<ItemsDomain> listCart2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listCart2.size(); i++) {
            fee = fee + (listCart2.get(i).getPrice() * listCart2.get(i).getNumberinCart());
        }
        return fee;
    }

    public void clearCart() {
        tinyDB.remove("CartList");
        Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show();
    }


}
