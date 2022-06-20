package com.example.sms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
  private Context context;
  private int layout;
  private ImageView mImageView;

  private ArrayList<String> names;
  private ArrayList<String> names2;
  public MyAdapter(Context context, int layout, ArrayList<String> names, ArrayList<String> names2){
    this.context = context;
    this.layout = layout;
    this.names = names;
    this.names2 = names2;

  }

  @Override
  public int getCount() {
    return this.names.size();
  }

  @Override
  public Object getItem(int position) {
    return this.names.get(position);
  }

  @Override
  public long getItemId(int id) {
    return id;
  }

  @Override

  public View getView(int position, View convertView, ViewGroup viewGroup) {
    // Copiamos la vista
    View v = convertView;

    //Inflamos la vista con nuestro propio layout
    LayoutInflater layoutInflater = LayoutInflater.from(this.context);

    v= layoutInflater.inflate(R.layout.list_item, null);
    // Valor actual según la posición

    String currentName = names.get(position);
    String currentName2 = names2.get(position);
    // mImageView = (ImageView) v.findViewById(R.id.imageView); mImageView.setImageResource(R.drawable.trash);
    // Referenciamos el elemento a modificar y lo rellenamos
    TextView textView = (TextView) v.findViewById(R.id.data1);
    textView.setText(currentName);
    TextView textView2 = (TextView) v.findViewById(R.id.data2);
    textView2.setText(currentName2);
    //Devolvemos la vista inflada
    return v;
  }}