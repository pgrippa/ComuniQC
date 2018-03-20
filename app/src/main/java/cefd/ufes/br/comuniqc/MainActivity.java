package cefd.ufes.br.comuniqc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import util.SpinnerItemAdapter;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText t_name;
    private EditText t_email;
    private EditText t_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t_name = findViewById(R.id.t_name);
        t_email = findViewById(R.id.t_email);
        t_msg = findViewById(R.id.t_msg);

        spinner = findViewById(R.id.spinner1);
        String[] items = (String[]) Arrays.asList(getResources().getStringArray(R.array.options_array)).toArray();

        spinner.setAdapter(new SpinnerItemAdapter(this, items));
        checkConnection();

    }

    private void checkConnection() {
        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.t_connerror_msg)
                    .setTitle(R.string.t_connerror)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void sendMessage(View v) {
        Log.i("SendMailActivity", "Send Button Clicked.");

        String opt = spinner.getSelectedItem().toString();
        String name = t_name.getText().toString();
        String email = t_email.getText().toString();
        String msg = t_msg.getText().toString();
        int index = spinner.getSelectedItemPosition();

        if (index == 0) {
            LinearLayout tv = (LinearLayout) spinner.getChildAt(0);
            ((TextView) tv.getChildAt(1))
                    .setError(getString(R.string.t_required));
            spinner.requestFocus();
            return;
        }

        if (!isValid(name)) {
            System.out.println(t_name.getText().toString());
            t_name.setError(getString(R.string.t_required));
            t_name.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            t_email.setError(getString(R.string.t_invalid_email));
            t_email.requestFocus();
            return;
        }

        if (!isValid(msg)) {
            t_msg.setError(getString(R.string.t_required));
            t_msg.requestFocus();
            return;
        }


        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.t_connerror_msg)
                    .setTitle(R.string.t_connerror)
                    .setPositiveButton("Aguardar Conexão", null)
                    .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            // MANDAR EMAIL

            new SendMailTask(MainActivity.this).execute(opt, name, email, msg);

            spinner.setSelection(0);
            t_name.getText().clear();
            t_email.getText().clear();
            t_msg.getText().clear();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.t_successmsg)
                    .setTitle(R.string.t_sentmsg)
                    .setPositiveButton("Nova Mensagem", null)
                    .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private boolean isValid(String field) {
        return (!field.isEmpty() || !field.trim().isEmpty());
    }

    private boolean isValidEmail(String email) {
        return (isValid(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


    public void exit(View v) {
        finish();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
