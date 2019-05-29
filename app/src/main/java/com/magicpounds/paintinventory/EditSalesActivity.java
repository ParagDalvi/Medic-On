package com.magicpounds.paintinventory;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EditSalesActivity extends AppCompatActivity {

    private static final int EXISTING_PAINT_LOADER = 0;
    EditText quantityEditText;
    Button reduceBtn;
    Button increaseBtn;
    FloatingActionButton saveFloatBtn;
    Spinner productSelectionSpinner;
    int initialQty = 0;
    int quantityAfterSale;
    TextView insufficientQty,inventoryQtyTextView ;



    private boolean mPaintHasChanged = false;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPaintHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sales);

        quantityEditText = (EditText) findViewById(R.id.quantityEditTextView);
        reduceBtn = (Button) findViewById(R.id.reduceQuantityBtn);
        increaseBtn = (Button) findViewById(R.id.increaseQuantityBtn);
        saveFloatBtn = (FloatingActionButton) findViewById(R.id.saveFloadtingBtn);
        insufficientQty = (TextView) findViewById(R.id.insufficentQtyTextView);
        inventoryQtyTextView = (TextView) findViewById(R.id.inventoryQtyTextView);

        setTitle("Make a Sale");

        quantityEditText.setText("0");

        productSelectionSpinner = (Spinner) findViewById(R.id.productSelectionSpinner);



        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String qty = quantityEditText.getText().toString();
                if(!TextUtils.isEmpty(qty))
                    initialQty = (int) Integer.parseInt(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        reduceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialQty--;

                if (initialQty < 0) {
                    initialQty = 0;
                }

                if (initialQty == 0) {
                    insufficientQty.setText("Quantity can't be 0 to make a sale");
                    insufficientQty.setVisibility(View.VISIBLE);
                } else {
                    insufficientQty.setVisibility(View.GONE);
                }

                quantityEditText.setText(Integer.toString(initialQty));
            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productName = productSelectionSpinner.getSelectedItem().toString();
                String[] simpllyArray = new String[1];
                simpllyArray[0] = productName;




                quantityEditText.setText(Integer.toString(initialQty));
            }
        });

        productSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String productName = productSelectionSpinner.getSelectedItem().toString();
                String[] simpllyArray = new String[1];
                simpllyArray[0] = productName;

                initialQty = 0;
                quantityEditText.setText(Integer.toString(initialQty));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        productSelectionSpinner.setOnTouchListener(mTouchListener);
        quantityEditText.setOnTouchListener(mTouchListener);
        increaseBtn.setOnTouchListener(mTouchListener);
        reduceBtn.setOnTouchListener(mTouchListener);


        saveFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_sales_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_sale) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mPaintHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}