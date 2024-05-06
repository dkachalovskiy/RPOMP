package com.example.minishop;

import java.io.Serializable;

public class Good implements Serializable {
    String name;
    int price;
    boolean checkbox;


    Good(String p_name, int p_price, boolean p_checkbox){
        name = p_name;
        price = p_price;
        checkbox = p_checkbox;
    }
}
