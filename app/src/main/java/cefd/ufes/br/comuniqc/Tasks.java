package cefd.ufes.br.comuniqc;

import android.os.AsyncTask;
import java.util.HashMap;
import java.util.Map;

public class Tasks {
    private AccessServiceAPI m_AccessServiceAPI;


    public void execNewFeedback(String type, String name, String email, String category, String message){
        new TaskNewFeedback().execute(type, name, email, category, message);

    }

    public class TaskNewFeedback extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }



        @Override
        protected Void doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "newfeedback");
            postParam.put("type", params[0]);
            postParam.put("name", params[1]);
            postParam.put("email", params[2]);
            postParam.put("category", params[3]);
            postParam.put("message", params[4]);

            try {
                m_AccessServiceAPI.getJSONStringWithParam_POST("https://suportecefd.000webhostapp.com/service.php", postParam);


            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }
    }
}
