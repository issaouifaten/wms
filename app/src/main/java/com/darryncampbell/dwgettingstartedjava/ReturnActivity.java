package com.darryncampbell.dwgettingstartedjava;

import static android.view.KeyEvent.KEYCODE_DEL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.darryncampbell.dwgettingstartedjava.LocalBase.Helper;
import com.darryncampbell.dwgettingstartedjava.Model.Article.Article;
import com.darryncampbell.dwgettingstartedjava.Model.Client.Client;
import com.darryncampbell.dwgettingstartedjava.Model.Return.LigneSelectReturn;
import com.darryncampbell.dwgettingstartedjava.Model.Return.ListLigneSelectReturn;
import com.darryncampbell.dwgettingstartedjava.Model.Return.ListSelectReturn;
import com.darryncampbell.dwgettingstartedjava.Model.Return.SelectReturn;
import com.darryncampbell.dwgettingstartedjava.Model.Transfert.LigneReturn;
import com.darryncampbell.dwgettingstartedjava.Model.prelevement.ListeBonCommande;
import com.darryncampbell.dwgettingstartedjava.Model.prelevement.Value;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnActivity extends AppCompatActivity implements View.OnTouchListener {
    Button btClient;
    Button btScan;
    Button btValid;
    Button btCancel;
    Button btSearch;
    TextView txtCodeClient, txtNomClient, txtCodePostalClient,txtCityClient,txtAdressClient;
    GridView gridReturn;
    final Context co = this;
    String baseUrlListClient = "";
    String baseUrlConsultArticle = "";
    String baseUrlCreateReturn = "";
    String baseUrlSelectReturn = "";
    String baseUrlLigneSelectReturn = "";
    ProgressBar progressBar;
    Helper helper;
    int scanView=0;
    androidx.appcompat.app.AlertDialog.Builder altGlobal;
    View px;
    EditText edtQt;
    EditText edtGln;
    EditText edtReference;
    androidx.appcompat.app.AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btClient = (Button) findViewById(R.id.bt_client);
        btScan = (Button) findViewById(R.id.bt_scan);
        btValid = (Button) findViewById(R.id.bt_valid);
        btCancel = (Button) findViewById(R.id.bt_cancel);
        btSearch = (Button) findViewById(R.id.bt_search);
        txtAdressClient = (TextView) findViewById(R.id.txt_address);
        txtCodePostalClient = (TextView) findViewById(R.id.txt_code_postal);
        txtCityClient = (TextView) findViewById(R.id.txt_city);
        txtCodeClient = (TextView) findViewById(R.id.txt_code_client);
        txtNomClient = (TextView) findViewById(R.id.txt_nom_client);
        gridReturn = (GridView) findViewById(R.id.grid_return);
        edtGln = (EditText) findViewById(R.id.edt_gln);
        edtReference = (EditText) findViewById(R.id.edt_reference);
        baseUrlListClient = getResources().getString(R.string.base_url) + "WmsApp_GetCustomerFromGLN?$format=application/json;odata.metadata=none";
        baseUrlConsultArticle = getResources().getString(R.string.base_url) + "WmsApp_GetItemFromBarcode?$format=application/json;odata.metadata=none";
        baseUrlCreateReturn = getResources().getString(R.string.base_url) + "WmsApp_CreateReturn?$format=application/json;odata.metadata=none";
        baseUrlSelectReturn = getResources().getString(R.string.base_url) + "WmsApp_SalesReturnOrderList?$format=application/json;odata.metadata=none";
        baseUrlLigneSelectReturn = getResources().getString(R.string.base_url) + "WmsApp_SelectedSalesReturnLigne?$format=application/json;odata.metadata=none";
        DWUtilities.CreateDWProfile(co, "com.return.action");
        btScan.setOnTouchListener(this);
        helper = new Helper(getApplicationContext());
        btClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helper.getListLigneReturn().getCount() > 0) {
                    final AlertDialog.Builder alt = new AlertDialog.Builder(co);
                    alt.setIcon(R.drawable.icon_return);
                    alt.setTitle("Annuler");
                    alt.setMessage("Voulez vous vraiment annuler le bon de retour ");
                    alt.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {

                                    helper.DeleteClientReturn();
                                    helper.DeleteLigneReturn();
                                    txtCodeClient.setText("");
                                    txtCityClient.setText("");
                                    txtCodePostalClient.setText("");
                                    txtAdressClient.setText("");
                                    txtNomClient.setText("");
                                    edtGln.setText("");

                                    FillListLigneReturn fillListLigneReturn = new FillListLigneReturn();
                                    fillListLigneReturn.execute("");
                                }
                            })
                            .setNegativeButton("Annuler",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface di, int i) {
                                            di.cancel();
                                        }
                                    });

                    final AlertDialog d = alt.create();
                    d.show();
                } else {
                    helper.DeleteClientReturn();
                    txtCodeClient.setText("");
                    txtCityClient.setText("");
                    txtCodePostalClient.setText("");
                    txtAdressClient.setText("");
                    txtNomClient.setText("");
                    edtGln.setText("");

                }


            }
        });
        btValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(helper.getListLigneReturn().getCount()>0)
                {
                CreateReturn();}else{
                    Toast.makeText(getApplicationContext(),"Entrer des articles d'abord", Toast.LENGTH_LONG).show();
                }
               //   FillListConsultListClient("3025591324608");

              // FillListConsultArticle("9782129822910");


            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.icon_return);
                alt.setTitle("Annuler");
                alt.setMessage("Voulez vous vraiment annuler le bon de retour ");
                alt.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {

                                helper.DeleteLigneReturn();
                                helper.DeleteClientReturn();
                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                        di.cancel();

                                    }
                                });

                final AlertDialog d = alt.create();
                d.show();

            }
        });
        Cursor cr = helper.getClientReturn();
        if (cr.getCount() > 0) {
            if (cr.move(1)) {
                txtCodeClient.setText(cr.getString(cr.getColumnIndex("NoClient")));
                txtNomClient.setText(cr.getString(cr.getColumnIndex("Designation")));

            }
        }


        FillListLigneReturn fillListLigneReturn = new FillListLigneReturn();
        fillListLigneReturn.execute("");
         altGlobal = new androidx.appcompat.app.AlertDialog.Builder(co);
        LayoutInflater li = LayoutInflater.from(co);
         px = li.inflate(R.layout.item_article, null);

        altGlobal.setIcon(R.drawable.icon_article);
        altGlobal.setTitle("Article");
        altGlobal.setView(px);

        // connectionClass = new ConnectionClass();

        edtQt = (EditText) px.findViewById(R.id.edt_qt_scan);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FillListConsultListClient(edtGln.getText().toString());
            }
        });


    }

    void FillListConsultArticle(final String scan) {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlConsultArticle;
            progressBar.setVisibility(View.VISIBLE);
            JSONObject jsonEAN = new JSONObject().put("EAN", scan);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputJson", jsonEAN.toString());
            Log.e("***input", jsonBody.toString());

            final String mRequestBody = jsonBody.toString();
            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****Response", response);
                            JSONObject obj = null;

                            try {
                                obj = new JSONObject(response);

                                Log.d("tag****Response", obj.getString("value"));
                                if (obj.getString("value").equals("Article Introuvable")) {
                                    scanView=0;
                                    Toast.makeText(getApplicationContext(), "Article Introuvable", Toast.LENGTH_SHORT).show();
                                } else {


                                    Article data = new Article();
                                    Gson gson = new Gson();
                                    data = gson.fromJson(obj.getString("value"), Article.class);
                                    final Article article = data;
                                    Log.d("tag****", article.toString());



                                    final TextView txtCode = (TextView) px.findViewById(R.id.txt_code_article);
                                    final TextView txtDesignation = (TextView) px.findViewById(R.id.txt_designation);
                                    txtCode.setText(article.getArticle());
                                    txtDesignation.setText(article.getDescription());
                                    Button btmoin = (Button) px.findViewById(R.id.btmoin);
                                    Button btplus = (Button) px.findViewById(R.id.btplus);

                                    if (helper.getLigneReturn(article.getArticle()).getCount() > 0) {

                                         Cursor c=  helper.getLigneReturn(article.getArticle());
                                         c.move(1);
                                         float qt=Float.parseFloat(c.getString(c.getColumnIndex("Quantite")));
                                         qt++;
                                         edtQt.setText(  ""+qt);

                                    }



                                        btplus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                if (!edtQt.getText().toString().equals("")) {
                                                    float qt = Float.parseFloat(edtQt.getText().toString());
                                                    qt++;
                                                    edtQt.setText(qt + "");
                                                }

                                            }
                                        });
                                        btmoin.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (!edtQt.getText().toString().equals("")) {
                                                    float qt = Float.parseFloat(edtQt.getText().toString());
                                                    qt--;
                                                    if (qt < 0) {
                                                        qt = 0;
                                                    } else {
                                                        edtQt.setText(qt + "");
                                                    }
                                                }

                                            }
                                        });

                                        altGlobal.setCancelable(false)
                                                .setPositiveButton("Ajouter",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface di, int i) {
                                                                if (edtQt.getText().toString().equals("0")) {
                                                                    Toast.makeText(getApplicationContext(), "La quantité doit etre différente de zéro", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    if (helper.getLigneReturn(article.getArticle()).getCount() > 0) {
                                                                        helper.UpdateLigneReturn(new LigneReturn(txtCodeClient.getText().toString(), txtCode.getText().toString(), edtQt.getText().toString()));
                                                                        //FillListLigneReturn fillListLigneReturn = new FillListLigneReturn();
                                                                       // fillListLigneReturn.execute("");
                                                                        scanView=0;
                                                                        finish();
                                                                        startActivity(getIntent());



                                                                    }else{
                                                                        helper.AddLigneReturn(new LigneReturn(txtCodeClient.getText().toString(), txtCode.getText().toString(), edtQt.getText().toString()));
                                                                    //FillListLigneReturn fillListLigneReturn = new FillListLigneReturn();
                                                                    //fillListLigneReturn.execute("");
                                                                    scanView=0;
                                                                        finish();
                                                                        startActivity(getIntent());

                                                                }
                                                                }
                                                            }
                                                        })
                                                .setNegativeButton("Annuler",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface di, int i) {
                                                                di.cancel();scanView=0;
                                                            }
                                                        });

                                        dialog = altGlobal.create();
                                        dialog.show();


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                scanView=0;
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                scanView=0;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            scanView=0;
                            Log.d("ERROR", "error => " + error.getLocalizedMessage());
                            Log.d("ERROR", "error => " + error.getMessage());
                            Toast.makeText(getApplicationContext(), " error api article : " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", getResources().getString(R.string.key_autorisation));
                    params.put("Content-Type", "application/json");
                    params.put("Company", getResources().getString(R.string.company_name));

                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        Log.e("ERROR", "errorgetbody => " + uee.toString());
                        return null;
                    }
                }
            };
            queue.add(getRequest);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());
            scanView=0;
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    void FillListConsultListClient(String scan) {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlListClient;
            progressBar.setVisibility(View.VISIBLE);
            JSONObject jsonEAN = new JSONObject().put("GLN", scan);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputJson", jsonEAN.toString());
            Log.e("***input", jsonBody.toString());

            final String mRequestBody = jsonBody.toString();
            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****Response", response);
                            JSONObject obj = null;

                            try {
                                obj = new JSONObject(response);

                                Log.d("tag****Response", obj.getString("value"));

                                Client data = new Client();
                                Gson gson = new Gson();
                                if(obj.getString("value").equals("Client Introuvable"))
                                {
                                    Toast.makeText(getApplicationContext(),"Client Introuvable",Toast.LENGTH_SHORT).show();
                                }else{
                                data = gson.fromJson(obj.getString("value"), Client.class);
                                helper.AddClient(data);
                                txtAdressClient.setText(data.getAddress());
                                txtCityClient.setText(data.getCity());
                                txtCodePostalClient.setText(data.getPostCode());
                                txtCodeClient.setText(data.getClient());
                                txtNomClient.setText(data.getDescription());}


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            Log.d("ERROR", "error => " + error.getLocalizedMessage());
                            Log.d("ERROR", "error => " + error.getMessage());
                            Toast.makeText(getApplicationContext(), " error api : " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", getResources().getString(R.string.key_autorisation));
                    params.put("Content-Type", "application/json");
                    params.put("Company", getResources().getString(R.string.company_name));

                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        Log.e("ERROR", "errorgetbody => " + uee.toString());
                        return null;
                    }
                }
            };
            queue.add(getRequest);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public class FillListLigneReturn extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
        ArrayList<String> list;
        Cursor cr;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPostExecute(String r) {


            progressBar.setVisibility(View.GONE);

            BaseAdapter baseAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return cr.getCount();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(final int pos, View convertView, ViewGroup parent) {
                    final LayoutInflater layoutInflater = LayoutInflater.from(co);
                    convertView = layoutInflater.inflate(R.layout.item_bc, null);

                    convertView = layoutInflater.inflate(R.layout.item_article, null);

                    final TextView txtCode = (TextView) convertView.findViewById(R.id.txt_code_article);
                    final TextView txtDesignation = (TextView) convertView.findViewById(R.id.txt_designation);
                    final EditText edt_qt_scan = (EditText) convertView.findViewById(R.id.edt_qt_scan);
                    final Button btmoin = (Button) convertView.findViewById(R.id.btmoin);
                    final Button btplus = (Button) convertView.findViewById(R.id.btplus);
                    cr = helper.getListLigneReturn();
                    if (cr.move(pos + 1)) {

                        txtCode.setText(cr.getString(cr.getColumnIndex("Article")));
                        edt_qt_scan.setText(cr.getString(cr.getColumnIndex("Quantite")));
                        txtDesignation.setText("");


                    }

                    edt_qt_scan.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {

                            String s = edt_qt_scan.getText().toString();
                            if (i != KEYCODE_DEL) {
                                if (!s.equals("")) {
                                    float qt = Float.valueOf(edt_qt_scan.getText().toString());
                                    helper.UpdateLigneReturn(new LigneReturn(txtCodeClient.getText().toString(), txtCode.getText().toString(), "" + qt));

                                }
                            }
                            return false;
                        }
                    });

                    btmoin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float qt = Float.valueOf(edt_qt_scan.getText().toString());
                            qt--;
                            if (qt < 0) {
                                qt = 0;
                            }
                            edt_qt_scan.setText("" + qt);
                            helper.UpdateLigneReturn(new LigneReturn(txtCodeClient.getText().toString(), txtCode.getText().toString(), "" + qt));


                        }
                    });
                    btplus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float qt = Float.valueOf(edt_qt_scan.getText().toString());
                            qt++;


                            edt_qt_scan.setText("" + qt);
                            helper.UpdateLigneReturn(new LigneReturn(txtCodeClient.getText().toString(), txtCode.getText().toString(), "" + qt));

                        }
                    });

                    return convertView;
                }
            };
            gridReturn.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                cr = helper.getListLigneReturn();


                if (cr.moveToFirst()) {
                    do {
                        Log.e("cursor", cr.getString(cr.getColumnIndex("Article")));
                    } while (cr.moveToNext());
                }


            } catch (Exception ex) {
                z = "list" + ex.toString();

            }
            return z;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        displayScanResult(intent);
    }

    private void displayScanResult(Intent scanIntent) {

        String decodedSource = scanIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_source));
        String decodedData = scanIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_data));
        String decodedLabelType = scanIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_label_type));
        String scan = decodedData;
        if(scan!=null) {
            if (txtCodeClient.getText().toString().equals("")) {
                FillListConsultListClient(scan);
            } else {
                if (scanView < 1) {
                    FillListConsultArticle(scan);
                    scanView++;
                } else {
                    float qt = Float.parseFloat(edtQt.getText().toString());
                    qt++;
                    edtQt.setText("" + qt);
                }
            }
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.btnScan) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //  Button pressed, start scan
                Intent dwIntent = new Intent();
                dwIntent.setAction("com.symbol.datawedge.api.ACTION");
                dwIntent.putExtra("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER", "START_SCANNING");
                sendBroadcast(dwIntent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                //  Button released, end scan
                Intent dwIntent = new Intent();
                dwIntent.setAction("com.symbol.datawedge.api.ACTION");
                dwIntent.putExtra("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER", "STOP_SCANNING");
                sendBroadcast(dwIntent);
            }
        }
        return true;
    }

    void CreateReturn() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlCreateReturn;
            progressBar.setVisibility(View.VISIBLE);
            JSONArray arrayJson = new JSONArray();
            Cursor cr = helper.getListLigneReturn();
            if (cr.moveToFirst()) {
                do {
                    JSONObject obj = new JSONObject().put("NoClient", cr.getString(cr.getColumnIndex("NoClient")))
                            .put("Quantite", cr.getString(cr.getColumnIndex("Quantite")))
                            .put("Article", cr.getString(cr.getColumnIndex("Article")))
                            .put("Reference",  edtReference.getText().toString());

                    arrayJson.put(obj);

                } while (cr.moveToNext());
            }
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputJson", arrayJson.toString());
            Log.d("*** create  ", jsonBody.toString());

            final String mRequestBody = jsonBody.toString();
            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****Response", response);

                            helper.DeleteClientReturn();
                            helper.DeleteLigneReturn();


                            Toast.makeText(getApplicationContext(), "retour crée avec succés", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            Log.d("ERROR", "error => " + error.getLocalizedMessage());
                            Log.d("ERROR", "error => " + error.getMessage());
                            Toast.makeText(getApplicationContext(), " error api : " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", getResources().getString(R.string.key_autorisation));
                    params.put("Content-Type", "application/json");
                    params.put("Company", getResources().getString(R.string.company_name));

                    return params;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        Log.e("ERROR", "errorgetbody => " + uee.toString());
                        return null;
                    }
                }
            };
            queue.add(getRequest);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    void FillListSelectReturn() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlSelectReturn;
            progressBar.setVisibility(View.VISIBLE);

            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****Response", response);
                            ListSelectReturn data = new ListSelectReturn();
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                Log.d("tag****Response", obj.getString("value"));
                                JSONArray array = new JSONArray(obj.getString("value"));

                                JSONObject jsonList = new JSONObject().put("value", array);


                                Gson gson = new Gson();
                                data = gson.fromJson(jsonList.toString(), ListSelectReturn.class);
                                final ListSelectReturn finalData = data;


                                Log.d("tag****", finalData.toString());
                                final BaseAdapter baseAdapter = new BaseAdapter() {
                                    @Override
                                    public int getCount() {
                                        return finalData.getValue().size();
                                    }

                                    @Override
                                    public Object getItem(int position) {
                                        return null;
                                    }

                                    @Override
                                    public long getItemId(int position) {
                                        return 0;
                                    }


                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        final LayoutInflater layoutInflater = LayoutInflater.from(co);
                                        convertView = layoutInflater.inflate(R.layout.item_simple, null);
                                        final TextView txt_doc = (TextView) convertView.findViewById(R.id.txt_title);
                                        final TextView txt_client = (TextView) convertView.findViewById(R.id.txt_description);
                                        final LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);

                                        final SelectReturn val = finalData.getValue().get(position);
                                        txt_doc.setText(val.getNoDoc());
                                        txt_client.setText(val.getNoClient());
                                        layout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                FillLigneSelectReturn();
                                            }
                                        });


                                        return convertView;
                                    }
                                };
                                gridReturn.setAdapter(baseAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            Log.d("ERROR", "error => " + error.toString());
                            Toast.makeText(getApplicationContext(), "eror vollety" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", getResources().getString(R.string.key_autorisation));
                    params.put("Content-Type", "application/json");
                    params.put("Company", getResources().getString(R.string.company_name));

                    return params;
                }

            };
            queue.add(getRequest);
        } catch (Exception e) {
            //  Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());

        }
    }
    void FillLigneSelectReturn() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlLigneSelectReturn;
            progressBar.setVisibility(View.VISIBLE);

            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****Response", response);
                            ListLigneSelectReturn data = new ListLigneSelectReturn();
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                Log.d("tag****Response", obj.getString("value"));
                                JSONArray array = new JSONArray(obj.getString("value"));

                                JSONObject jsonList = new JSONObject().put("value", array);


                                Gson gson = new Gson();
                                data = gson.fromJson(jsonList.toString(), ListLigneSelectReturn.class);
                                final ListLigneSelectReturn finalData = data;


                                Log.d("tag****", finalData.toString());
                                final BaseAdapter baseAdapter = new BaseAdapter() {
                                    @Override
                                    public int getCount() {
                                        return finalData.getValue().size();
                                    }

                                    @Override
                                    public Object getItem(int position) {
                                        return null;
                                    }

                                    @Override
                                    public long getItemId(int position) {
                                        return 0;
                                    }


                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        final LayoutInflater layoutInflater = LayoutInflater.from(co);
                                        convertView = layoutInflater.inflate(R.layout.item_ligne_return, null);
                                        final TextView txt_doc = (TextView) convertView.findViewById(R.id.txt_no_doc);
                                        final TextView txt_ean = (TextView) convertView.findViewById(R.id.txt_ean);
                                        final TextView txt_code_article = (TextView) convertView.findViewById(R.id.txt_code_article);
                                        final TextView txt_qt = (TextView) convertView.findViewById(R.id.txt_qt);

                                        final LigneSelectReturn val = finalData.getValue().get(position);
                                        txt_doc.setText(val.getNoDoc());
                                        txt_code_article.setText(val.getArticle());
                                        txt_ean.setText(val.getEAN());
                                        txt_qt.setText(val.getQuantite());


                                        return convertView;
                                    }
                                };
                                gridReturn.setAdapter(baseAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            Log.d("ERROR", "error => " + error.toString());
                            Toast.makeText(getApplicationContext(), "eror vollety" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", getResources().getString(R.string.key_autorisation));
                    params.put("Content-Type", "application/json");
                    params.put("Company", getResources().getString(R.string.company_name));

                    return params;
                }

            };
            queue.add(getRequest);
        } catch (Exception e) {
            //  Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());

        }
    }
}