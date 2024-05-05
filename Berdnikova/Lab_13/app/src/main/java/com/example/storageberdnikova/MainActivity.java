package com.example.storageberdnikova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editTextID;
    Button butDownload;
    Button butSee;
    Button butDelete;
    private boolean showPopup;
    private PopupWindow popupWindow;
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 100;
    private boolean isPopupShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextID = findViewById(R.id.editID);
        butDownload = findViewById(R.id.butDownload);

        butSee = findViewById(R.id.butSee);
        butSee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openPdfFile();
            }
        });

        butDelete = findViewById(R.id.butDelete);
        butDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String fileName = "journal_" + editTextID.getText().toString() + ".pdf";
                deletePdfFile(fileName);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (!isPopupShown && hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
            } else {
                showPopupWindow();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Permission not detected", Toast.LENGTH_SHORT).show();
            } else {
                showPopupWindow();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showPopupWindow()
    {
        isPopupShown = true;
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupView = layoutInflater.inflate(R.layout.popup_window, null);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        Button buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("showPopup", false);
                editor.apply();
                showPopup = false;
                popupWindow.dismiss();
            }
        });
        View rootView = getWindow().getDecorView().getRootView();
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    public void GetID(View view)
    {
        EditText editText = findViewById(R.id.editID);
        editText.setFilters(new InputFilter[]{ new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                StringBuilder filtered = new StringBuilder();
                for (int i = start; i < end; i++)
                {
                    char character = source.charAt(i);
                    if (Character.isDigit(character))
                    {
                        filtered.append(character);
                    }
                }
                return filtered.toString();
            }
        }});

        butDownload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String inputID = editTextID.getText().toString();
                if (!inputID.isEmpty())
                {
                    Log.d("MainActivity", "Request: journal № " + inputID);

                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                        Toast.makeText(MainActivity.this, "Give permission to download file", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "File is downloading (Berdnikova)", Toast.LENGTH_SHORT).show();
                        downloadJournal(inputID);
                    }
                } else {
                    android.widget.Toast.makeText(MainActivity.this, "Enter file(journal) ID", android.widget.Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void downloadJournal(String inputID)
    {
        String fileUrl = "https://ntv.ifmo.ru/file/journal/" + inputID + ".pdf";
        Log.d("MainActivity", "New url:  " + fileUrl);

        DownloadFileAsync downloadFileAsync = new DownloadFileAsync(this);
        downloadFileAsync.execute(fileUrl);
    }

    private class DownloadFileAsync extends AsyncTask<String, String, String>
    {
        private Context context;
        public DownloadFileAsync(Context context)
        {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params)
        {
            int count;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept", "application/pdf");
                connection.connect();

                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 81920);
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String fileName = "journal_" + editTextID.getText().toString() + ".pdf";
                File outputFile = new File(directory, fileName);
                FileOutputStream output = new FileOutputStream(outputFile);

                byte[] data = new byte[81920];
                long total = 0;
                while ((count = input.read(data)) != -1)
                {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return "File saved in Downloads";

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return "Error!!";
            }
        }
        @Override
        protected void onPostExecute(String result)
        {
            butSee.setEnabled(true);
            butDelete.setEnabled(true);
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }

    public void openPdfFile()
    {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "journal_" + editTextID.getText().toString() + ".pdf";
        String filePath = directory + File.separator + fileName;

        File file = new File(filePath);
        if (!file.exists())
        {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri fileUri = FileProvider.getUriForFile(this, "com.yourpackagename.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePdfFile(String fileName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm to delete")
                .setMessage("Do you want to delete file?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doDelete(fileName);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void doDelete(String fileName)
    {
        Context context = getApplicationContext();
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        if (file.exists())
        {
            Uri fileUri = FileProvider.getUriForFile(context, "com.example.filedown.provider", file);
            if (file.delete())
            {
                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
                Log.d("Delete File", "File deleted successfully");
                Toast.makeText(context, "File successfully was deleted", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Delete File", "Failed to delete file");
                Toast.makeText(context, "Could not delete file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("Delete File", "File does not exist");
            Toast.makeText(context, "File does not exist", Toast.LENGTH_SHORT).show();
        }
    }
}


