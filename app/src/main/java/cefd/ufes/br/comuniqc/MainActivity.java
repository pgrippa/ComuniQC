package cefd.ufes.br.comuniqc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import util.SpinnerItemAdapter;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;

    private Spinner spinner;
    private EditText t_name;
    private EditText t_email;
    private EditText t_msg;
    private RadioButton student;
    private RadioButton professor;
    private RadioButton tae;
    private RadioButton other;

    RadioGroup category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);

        t_name = findViewById(R.id.t_name);
        t_email = findViewById(R.id.t_email);
        t_msg = findViewById(R.id.t_msg);

        student = findViewById(R.id.cb_student);
        professor = findViewById(R.id.cb_professor);
        tae = findViewById(R.id.cb_tae);
        other = findViewById(R.id.cb_other);

        category = findViewById(R.id.rg_category);

        spinner = findViewById(R.id.spinner1);
        String[] items = (String[]) Arrays.asList(getResources().getStringArray(R.array.options_array)).toArray();

        spinner.setAdapter(new SpinnerItemAdapter(this, items));
        checkConnection();
    }

    @Override
    protected  void onResume(){
        super.onResume();

        String name = prefs.getString("name","");
        String email = prefs.getString("email","");
        int cat = prefs.getInt("category",-1);

        t_name.setText(name);
        t_email.setText(email);
    }

    private void checkConnection() {
        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.t_connerror_msg)
                    .setTitle(R.string.t_connerror)
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
            t_name.setError(getString(R.string.t_required));
            t_name.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            t_email.setError(getString(R.string.t_invalid_email));
            t_email.requestFocus();
            return;
        }

        if(!isValidCategory()){
            student.setError(getString(R.string.t_select_cat));
            category.requestFocus();
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
                    .setPositiveButton(getString(R.string.waitconn), null)
                    .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            saveInfo();

            spinner.setSelection(0);
            t_msg.getText().clear();

            int catId = category.getCheckedRadioButtonId();
            RadioButton btn = findViewById(catId);

            String cat = btn.getText().toString();

            Tasks  task = new Tasks();

            task.execNewFeedback(opt, name, email,cat, msg);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.t_successmsg)
                    .setTitle(R.string.t_sentmsg)
                    .setPositiveButton(getString(R.string.newmessage), null)
                    .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
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

    private boolean isValidCategory(){
        return (student.isChecked() || professor.isChecked() || tae.isChecked() || other.isChecked());
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

    private void saveInfo(){
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("name",t_name.getText().toString());
        ed.putString("email",t_email.getText().toString());
        ed.putInt("category",category.getCheckedRadioButtonId());
        ed.commit();
    }
}
