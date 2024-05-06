package com.example.lab29;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddressDialogFragment extends DialogFragment {
    private EditText fromAddressEditText;
    private EditText toAddressEditText;
    private Button sendButton;

    public interface AddressDialogListener {
        void onAddressesSelected(String startAddress, String endAddress);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.address_dialog, null);

        fromAddressEditText = dialogView.findViewById(R.id.from_address_edittext);
        toAddressEditText = dialogView.findViewById(R.id.to_address_edittext);

        builder.setView(dialogView)
                .setTitle("Enter addresses")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("Build route", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fromAddress = fromAddressEditText.getText().toString();
                        String toAddress = toAddressEditText.getText().toString();

                        AddressDialogListener listener = (AddressDialogListener) getActivity();
                        assert listener != null;
                        listener.onAddressesSelected(fromAddress, toAddress);

                        dismiss();
                    }
                });


        return builder.create();
    }
}
