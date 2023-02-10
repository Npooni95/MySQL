package edu.csustan.cs3810.mysql;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Container extends AsyncTask<String, Void, String> {
    private TextView lblMsg;

    private static final String PROTOCOL = "http://";
    private static final String IP       = "34.82.21.121";

    private static final String FILE_NAME = "runQuery.php";
    private static final String HTTP_POST_METHOD = "POST";

    public Container(TextView lblMsg) {
        this.lblMsg = lblMsg;
    }

    private String getURL(String fileName) {
        return PROTOCOL + IP + "/" + fileName;
    }

    private HttpURLConnection establishConnection() {
        try {
            URL url = new URL(getURL(FILE_NAME));
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            return  conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendRequest(HttpURLConnection conn, String type, String query) {
        try {
            conn.setRequestMethod(HTTP_POST_METHOD);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream output = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            String request = "type=" + type + "&query=" +query;
            Log.i("Container", request);
            writer.write(request);
            writer.flush();

            writer.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getResponse(HttpURLConnection conn) {
        String result = "";

        try {
            InputStream input = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            Log.i("Container",result);

            reader.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String runQueryCommon(String query, String type) {
        String result = "";

        HttpURLConnection conn = establishConnection();

        if (conn == null) {
            return "no connection";
        }

        sendRequest(conn, type, query);
        result = getResponse(conn);
        conn.disconnect();
        return result;
    }

    public String runQuery(String query) {
        String result = "";

        if (query.contains("create")) {
            result = runQueryCommon(query, "createTable");
        } else if (query.contains("drop")) {
            result = runQueryCommon(query,"deleteTable");
        } else if (query.contains("insert")) {
            result = runQueryCommon(query,"insert");
        } else if(query.contains("update")) {
            result = runQueryCommon(query,"update");
        } else if (query.contains("delete")) {
            result = runQueryCommon(query,"delete");
        } else if (query.contains("select")) {
            result = runQueryCommon(query,"select");
            result = (Html.fromHtml(result)).toString();
        } else {
            result = "no create/drop/insert/update/delete/select";
        }
        return result;
    }


    @Override
    protected String doInBackground(String... params) {
        String query = params[0];
        String result = runQuery(query);
        Log.i("Container:Back",result);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("Container:Post", result);
        lblMsg.setText(result);
    }
}
