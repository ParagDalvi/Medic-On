package com.magicpounds.paintinventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity{


    private Uri mCurrentPetUri = null;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    EditText nameEditText, catEditText;
    EditText quantityEditText;
    EditText costEditText;
    Button reduceBtn;
    Button increaseBtn;
    FloatingActionButton saveFloatBtn;
    int initialQty = 0;

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
        setContentView(R.layout.activity_edit);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Products");


        nameEditText = (EditText) findViewById(R.id.nameEditText);
        catEditText = (EditText) findViewById(R.id.categoryEditText);
        quantityEditText = (EditText) findViewById(R.id.quantityEditTextView);
        costEditText = (EditText) findViewById(R.id.costEditText);
        reduceBtn = (Button) findViewById(R.id.reduceQuantityBtn);
        increaseBtn = (Button) findViewById(R.id.increaseQuantityBtn);
        saveFloatBtn = (FloatingActionButton) findViewById(R.id.saveFloadtingBtn);

        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if (mCurrentPetUri == null) {
            setTitle(R.string.add_new_paint);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.edit_existing_product);
        }


        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initialQty = (int) Integer.parseInt(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        reduceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPetUri == null) {
                    initialQty--;
                    if (initialQty < 1) {
                        initialQty = 1;
                    }
                    quantityEditText.setText(Integer.toString(initialQty));
                } else {
                    initialQty = Integer.parseInt(quantityEditText.getText().toString());
                    initialQty--;
                    if (initialQty < 1) {
                        initialQty = 1;
                    }
                    quantityEditText.setText(Integer.toString(initialQty));
                }
            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPetUri == null) {
                    initialQty++;
                    if (initialQty < 1) {
                        initialQty = 1;
                    }
                    quantityEditText.setText(Integer.toString(initialQty));
                } else {
                    initialQty = Integer.parseInt(quantityEditText.getText().toString());
                    initialQty++;
                    if (initialQty < 1) {
                        initialQty = 1;
                    }
                    quantityEditText.setText(Integer.toString(initialQty));
                }
            }
        });


        nameEditText.setOnTouchListener(mTouchListener);
        quantityEditText.setOnTouchListener(mTouchListener);
        costEditText.setOnTouchListener(mTouchListener);
        increaseBtn.setOnTouchListener(mTouchListener);
        reduceBtn.setOnTouchListener(mTouchListener);

        saveFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product(catEditText.getText().toString(), costEditText.getText().toString()
                        , nameEditText.getText().toString(), quantityEditText.getText().toString());
                // Clear input box
                mMessagesDatabaseReference.push().setValue(product);
                finish();
                Toast.makeText(EditActivity.this, "Product saved", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.menuItemDeleteOne:

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.savePaint:
                // Do nothing for now
                Product product = new Product(catEditText.getText().toString(), costEditText.getText().toString()
                        , nameEditText.getText().toString(), quantityEditText.getText().toString());
                // Clear input box
                mMessagesDatabaseReference.push().setValue(product);
                finish();
                Toast.makeText(this, "Product Saved", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mPaintHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentPetUri == null) {
            MenuItem menuItem = menu.findItem(R.id.menuItemDeleteOne);
            menuItem.setVisible(false);
        }
        return true;
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
