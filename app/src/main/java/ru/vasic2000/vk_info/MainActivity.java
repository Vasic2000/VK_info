package ru.vasic2000.vk_info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static ru.vasic2000.vk_info.utils.NetworkUtils.generateURL;
import static ru.vasic2000.vk_info.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {
    private EditText searchField;
    private Button searchButton;
    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadIndicator;

    private void showResult() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_VK_Search);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL generatedURL = generateURL(searchField.getText().toString());
                new VKQuerryTask().execute(generatedURL);
            }
        };

        searchButton.setOnClickListener(onClickListener);
    }

    class VKQuerryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            loadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String firstName;
            String lastName;
            String resulting;
            StringBuilder res = new StringBuilder();

            if(response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userInfo = jsonArray.getJSONObject(i);

                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");

                        res.append("Имя: " + firstName + "\nФамилия: " + lastName + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resulting = res.toString();
                result.setText(resulting);
                showResult();
            } else {
                showError();
            }
            loadIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
