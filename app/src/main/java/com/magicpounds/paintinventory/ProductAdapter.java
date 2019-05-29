package com.magicpounds.paintinventory;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {


    public ProductAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_view_inventory, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.productNameTextView);
        TextView costTextView = (TextView) convertView.findViewById(R.id.productCostTextView);
        TextView quantityTextView = (TextView) convertView.findViewById(R.id.quantityTextView);
        TextView catTExtView = (TextView) convertView.findViewById(R.id.category);

        Product product = getItem(position);

        nameTextView.setText(product.getName());
        costTextView.setText(product.getCost());
        quantityTextView.setText(product.getQuantity());
        catTExtView.setText("Product (" + product.getCategory() + ")");

        return convertView;
    }
}
