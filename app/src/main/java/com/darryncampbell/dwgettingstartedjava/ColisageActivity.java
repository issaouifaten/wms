package com.darryncampbell.dwgettingstartedjava;

import static android.view.KeyEvent.KEYCODE_DEL;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
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
import com.darryncampbell.dwgettingstartedjava.Model.Colis.LigneColisCreated;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.LigneColis;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.ListColis;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.ListColisCreated;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.ListPreparation;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.ListSearchColi;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.PreparationColisLigne;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.SearchColis;
import com.darryncampbell.dwgettingstartedjava.Model.TypeColis.TypeColis;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColisageActivity extends Activity implements View.OnTouchListener {
    final Context co = this;
    GridView grid_prelevement;
    ProgressBar progressBar;
    Helper helper;

    Button bt_scan, btnDelete, bt_create, btn_list;
    String searchScan = "";
    TextView output, txt_no_doc, txt_no_colis, txt_poids_max;
    String baseUrlLigneColisToScan = "";
    String baseUrlCreatePrelevement = "";
    String baseUrlListPrelevement = "";
    String baseUrlListColisCreated = "";
    String baseUrlTypeColis = "";
    String baseUrlPreparationColis = "";
    String baseUrlSearchColis = "";
    Boolean select_prelevement = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colisage);
        baseUrlLigneColisToScan = getResources().getString(R.string.base_url) + "WmsApp_ReturnLigneColisage?$format=application/json;odata.metadata=none";
        baseUrlCreatePrelevement = getResources().getString(R.string.base_url) + "WmsApp_InsertLigneColis?$format=application/json;odata.metadata=none";
        baseUrlListPrelevement = getResources().getString(R.string.base_url) + "WmsApp_RegistredPick?$format=application/json;odata.metadata=none";
        baseUrlListColisCreated = getResources().getString(R.string.base_url) + "WmsApp_ReturnColis?$format=application/json;odata.metadata=none";
        baseUrlTypeColis = getResources().getString(R.string.base_url) + "TypeColis?$format=application/json;odata.metadata=none";
        baseUrlPreparationColis = getResources().getString(R.string.base_url) + "WmsApp_CreateColis?$format=application/json;odata.metadata=none";
        baseUrlSearchColis = getResources().getString(R.string.base_url) + "WmsApp_SelectColisFromSSCC?$format=application/json;odata.metadata=none";
        grid_prelevement = (GridView) findViewById(R.id.grid_prelevement);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        output = findViewById(R.id.txtOutput);
        txt_poids_max = (TextView) findViewById(R.id.txt_poids_max);
        txt_no_colis = (TextView) findViewById(R.id.txt_no_colis);
        txt_no_doc = (TextView) findViewById(R.id.txt_no_doc);
        helper = new Helper(getApplicationContext());

        bt_create = (Button) findViewById(R.id.btn_create);
        bt_scan = (Button) findViewById(R.id.btnScanprev);
        btn_list = (Button) findViewById(R.id.btn_list);

        DWUtilities.CreateDWProfile(co, "com.colisage.action");
        Button btnScan = findViewById(R.id.btnScanprev);
        btnScan.setOnTouchListener(this);

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              initList();


            }
        });


        btnDelete = (Button) findViewById(R.id.bt_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.ison_prelevement);
                alt.setTitle("Preparation colis");
                alt.setMessage("Voulez vous vraiment annuler ce colis");
                alt.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {

                                helper.DeleteListBonCommandPrelevementColisage();
                                helper.DeleteLigneBonCommandPrelevementColisage();
                                helper.DeleteValideBonCommandPrelevementColisage();
                                helper.DeleteLigneColisCreated();
                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                    di.cancel();
//                                        searchScan = "3274080005003";
//
//                                        output.setText("3274080005003");
//                                        Log.e("txt_no_colis",txt_no_colis.getText().toString());
//                                        if(txt_no_colis.getText().toString().equals(""))
//                                        {Toast.makeText(getApplicationContext(),"search",Toast.LENGTH_SHORT).show();
//
//                                        searchColi();
//
//
//                                        }else{
//                                           FillListColisToScanSearch fillListColisToScanSearch = new FillListColisToScanSearch();
//                                            fillListColisToScanSearch.execute("");
//                                        }

                                    }
                                });

                final AlertDialog d = alt.create();
                d.show();

            }
        });
        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor c = helper.getListLigneCommandPrelevementColisage();
                if (c.getCount() > 0) {
                    CreatePrelevementColis();


                } else {
                    Toast.makeText(getApplicationContext(), "scanner des article d'abord", Toast.LENGTH_SHORT).show();

                }
            }
        });
        initList();

    }

    void initList() {
        select_prelevement = helper.getListCommandPrelevementColisage().getCount() > 0;
        if (!select_prelevement) {
            FillListBonPrelevement();
        } else {
            Cursor cr = helper.getListCommandPrelevementColisage();

            String numDoc = "";
            String NoColis = "";
            String TypeColis = "";
            String PoidsMax = "";
            if (cr.move(1)) {
                numDoc = cr.getString(cr.getColumnIndex("NoDoc"));
                NoColis = cr.getString(cr.getColumnIndex("NoColis"));
                TypeColis = cr.getString(cr.getColumnIndex("TypeColis"));
                PoidsMax = cr.getString(cr.getColumnIndex("PoidsMax"));
                txt_no_colis.setText(NoColis);
                txt_no_doc.setText(numDoc);
                txt_poids_max.setText(PoidsMax);
                if (numDoc.equals(""))
                    FillListBonPrelevement();
                else if (TypeColis.equals(""))
                    FillListTypeColis();
                else if (NoColis!=null&&NoColis.equals(""))
                    PreparationColis();
                else if (helper.getListLigneCommandPrelevementColisage().getCount() > 0) {
                    FillListColisToScan fillListColisToScan = new FillListColisToScan();
                    fillListColisToScan.execute("");

                } else {
                    FillLigneColisToScan();
                }

            }
        }
    }

    float calculPoidsTotal() {
        float poidsTotal = 0;
        try {
            Cursor cr = helper.getListLigneCommandPrelevementColisage();



            if (cr.moveToFirst()) {
                do {
                    if (!cr.getString(cr.getColumnIndex("poidsUnite")).equals(""))
                        poidsTotal += cr.getInt(cr.getColumnIndex("QuantiteScan")) * Float.parseFloat(cr.getString(cr.getColumnIndex("poidsUnite")));

                } while (cr.moveToNext());
            }



        } catch (Exception ex) {


        }

        return poidsTotal;
    }


    void FillListBonPrelevement() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlListPrelevement;
            progressBar.setVisibility(View.VISIBLE);

            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****Response", response);
                            ListPreparation data = new ListPreparation();
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                Log.d("tag****Response", obj.getString("value"));
                                JSONArray array = new JSONArray(obj.getString("value"));

                                JSONObject jsonList = new JSONObject().put("value", array);


                                Gson gson = new Gson();
                                data = gson.fromJson(jsonList.toString(), ListPreparation.class);
                                final ListPreparation finalData = data;


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
                                        convertView = layoutInflater.inflate(R.layout.item_bc, null);
                                        final TextView txt_code = (TextView) convertView.findViewById(R.id.txt_code);
                                        final TextView txt_nom_client = (TextView) convertView.findViewById(R.id.tx_nom_client);
                                        final Button btplus = (Button) convertView.findViewById(R.id.btplus);
                                        final Button btdelete = (Button) convertView.findViewById(R.id.btdelete);

                                        final PreparationColisLigne val = finalData.getValue().get(position);
                                        if (helper.testExistBonCommandPrelevementColisage(val.getNoDoc())) {
                                            btdelete.setVisibility(View.VISIBLE);
                                            btplus.setVisibility(View.GONE);
                                        } else {
                                            btdelete.setVisibility(View.GONE);
                                            btplus.setVisibility(View.VISIBLE);
                                        }

                                        btplus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                helper.DeleteListBonCommandPrelevementColisage();
                                                btdelete.setVisibility(View.VISIBLE);
                                                btplus.setVisibility(View.GONE);
                                                txt_no_doc.setText(txt_code.getText().toString());
                                                helper.AddBonCommandePrelevementColisage(new PreparationColisLigne(txt_code.getText().toString(), "", "", ""));
                                               if(helper.getListColisCreated().getCount()<=0)
                                               {
                                                FillListTypeColis();}

                                            }
                                        });
                                        btdelete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                helper.DeleteListBonCommandPrelevementColisage();
                                                helper.RemoveBonCommandPrelevementColisage(val);
                                                btdelete.setVisibility(View.GONE);
                                                btplus.setVisibility(View.VISIBLE);
                                            }
                                        });

                                        txt_code.setText(val.getNoDoc() + "");
                                        txt_nom_client.setText("");


                                        return convertView;
                                    }
                                };
                                grid_prelevement.setAdapter(baseAdapter);
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

    void FillListTypeColis() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlTypeColis;
            progressBar.setVisibility(View.VISIBLE);


            StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****Response", response);
                            TypeColis data = new TypeColis();
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                Log.d("tag****Response", obj.getString("value"));
                                JSONArray array = new JSONArray(obj.getString("value"));

                                JSONObject jsonList = new JSONObject().put("value", array);


                                Gson gson = new Gson();
                                data = gson.fromJson(jsonList.toString(), TypeColis.class);
                                final TypeColis finalData = data;


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
                                        convertView = layoutInflater.inflate(R.layout.item_type_colis, null);
                                        final TextView txt_type = (TextView) convertView.findViewById(R.id.txt_type);
                                        final TextView txt_volume = (TextView) convertView.findViewById(R.id.txt_volume);
                                        final TextView bt_add = (TextView) convertView.findViewById(R.id.bt_add);

                                        final com.darryncampbell.dwgettingstartedjava.Model.TypeColis.Value val = finalData.getValue().get(position);
                                        txt_type.setText(val.getType() + "");
                                        txt_volume.setText(val.getPoids() + "");
                                        bt_add.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Cursor cr = helper.getListCommandPrelevementColisage();


                                                if (cr.move(1)) {
                                                    String numDoc = cr.getString(cr.getColumnIndex("NoDoc"));

                                                    txt_no_doc.setText(numDoc);
                                                    txt_poids_max.setText(txt_volume.getText().toString());


                                                    Toast.makeText(getApplicationContext(), numDoc + "***" + txt_type.getText().toString(), Toast.LENGTH_SHORT).show();
                                                    helper.UpdateBonCommandePrelevementColisage(new PreparationColisLigne(numDoc, txt_type.getText().toString(), "", txt_volume.getText().toString()));
                                                    PreparationColis();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "count" + cr.getCount(), Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });

                                        return convertView;
                                    }
                                };
                                grid_prelevement.setAdapter(baseAdapter);
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

    void FillLigneColisToScan() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlLigneColisToScan;
            progressBar.setVisibility(View.VISIBLE);
            Cursor cr = helper.getListCommandPrelevementColisage();

            String numDoc = "";
            String NoColis = "";
            if (cr.move(1)) {
                numDoc = cr.getString(cr.getColumnIndex("NoDoc"));
                NoColis = cr.getString(cr.getColumnIndex("NoColis"));


            }
            JSONObject json = new JSONObject().put("NoDoc", numDoc).put("NoColis", NoColis);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputJson", json.toString());

            final String mRequestBody = jsonBody.toString();
            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****ResponseFillLignecolis", response);


                            bt_scan.setVisibility(View.VISIBLE);
                            JSONObject obj = null;
                            ListColis data = new ListColis();
                            try {
                                obj = new JSONObject(response);
                                Log.d("tag****Response", obj.getString("value"));
                                JSONArray array = new JSONArray(obj.getString("value"));

                                JSONObject jsonList = new JSONObject().put("value", array);


                                Gson gson = new Gson();
                                data = gson.fromJson(jsonList.toString(), ListColis.class);
                                final ListColis finalData = data;
                                for (int i = 0; i < finalData.getValue().size(); i++) {
                                    LigneColis ligne = finalData.getValue().get(i);
                                    ligne.setQuantiteScan("0");
                                    helper.AddLigneBonCommandePrelevementColisage(ligne);
                                }

                                FillListColisToScan fillListColisToScan = new FillListColisToScan();
                                fillListColisToScan.execute("");
                            } catch (JSONException e) {
                                Log.e("errorlistlignebc", e.toString());
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
            // Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());

        }
    }

    void FillColisCreated() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlListColisCreated;
            progressBar.setVisibility(View.VISIBLE);
            Cursor cr = helper.getListCommandPrelevementColisage();
            cr.move(1);
            String numDoc = cr.getString(cr.getColumnIndex("NoDoc"));
            JSONObject json = new JSONObject().put("NoDoc", numDoc);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputJson", json.toString());

            final String mRequestBody = jsonBody.toString();
            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****ResponseFillLignecolisGenerated", response);

                            bt_scan.setVisibility(View.VISIBLE);
                            JSONObject obj = null;
                            ListColisCreated data = new ListColisCreated();
                            try {
                                obj = new JSONObject(response);
                                Log.d("tag****Response", obj.getString("value"));
                                JSONArray array = new JSONArray(obj.getString("value"));

                                JSONObject jsonList = new JSONObject().put("value", array);


                                Gson gson = new Gson();
                                data = gson.fromJson(jsonList.toString(), ListColisCreated.class);
                                final ListColisCreated finalData = data;
                                if(!(helper.getListColisCreated().getCount()>0))
                                { for (int i = 0; i < finalData.getValue().size(); i++) {
                                    LigneColisCreated ligne = finalData.getValue().get(i);

                                    helper.AddLigneColisCreated(ligne);
                                }}
                                FillListColisCreated fillListColisCreated = new FillListColisCreated();
                                fillListColisCreated.execute("");

                            } catch (JSONException e) {
                                Log.e("errorlistlignebc", e.toString());
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
            // Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());

        }
    }
    void searchColi() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlSearchColis;
            progressBar.setVisibility(View.VISIBLE);

            JSONObject json = new JSONObject().put("SSCC", output.getText().toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputJson", json.toString());

            final String mRequestBody = jsonBody.toString();
            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****searchcolis", response);

                            bt_scan.setVisibility(View.VISIBLE);
                            JSONObject obj = null;
                            SearchColis data = new SearchColis();
                            try {
                                obj = new JSONObject(response);
                                Log.d("tag****Response", obj.getString("value"));


                                Gson gson = new Gson();
                                data = gson.fromJson(obj.getString("value"), SearchColis.class);
                                final SearchColis finalData = data;
                                Log.d("tag****Response", ""+finalData.getColis());
                                txt_no_colis.setText(finalData.getColis());
                                Cursor cr = helper.getListCommandPrelevementColisage();

                                String numDoc = "";
                                String NoColis = "";
                                String TypeColis = "";
                                String PoidsMax = "";
                                if (cr.move(1)) {
                                    numDoc = cr.getString(cr.getColumnIndex("NoDoc"));
                                    NoColis = cr.getString(cr.getColumnIndex("NoColis"));
                                    TypeColis = cr.getString(cr.getColumnIndex("TypeColis"));
                                    PoidsMax = cr.getString(cr.getColumnIndex("PoidsMax"));
                                    txt_no_colis.setText(finalData.getColis());
                                helper.UpdateBonCommandePrelevementColisage(new PreparationColisLigne(numDoc, TypeColis,finalData.getColis(), PoidsMax));
                                FillLigneColisToScan();
                                }
                            } catch (JSONException e) {
                                Log.e("errorlistlignebc", e.toString());
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
            // Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.toString());

        }
    }
    void PreparationColis() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlPreparationColis;
            progressBar.setVisibility(View.VISIBLE);
            Cursor cr = helper.getListCommandPrelevementColisage();
            cr.move(1);
            String numDoc = cr.getString(cr.getColumnIndex("NoDoc"));
            String typeColis = cr.getString(cr.getColumnIndex("TypeColis"));
            JSONObject json = new JSONObject().put("NoDoc", numDoc).put("TypeColis", typeColis);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputJson", json.toString());
            Log.e("inputjson", jsonBody.toString());

            final String mRequestBody = jsonBody.toString();
            StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressBar.setVisibility(View.GONE);
                            Log.d("tag****ResponsePreparationColis", response);

                            FillColisCreated();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            Log.d("***ERROR", "error => " + error.toString());
                            Log.d("***mRequestBody", "error => " + mRequestBody);
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
            // Toast.makeText(getApplicationContext(), "eror exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("*****error", e.toString());

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
        // String scan = decodedData + " [" + decodedLabelType + "]\n\n";
        String scan = decodedData;
        searchScan = scan;

        output.setText(scan);
        if(txt_no_colis.getText().toString().equals(""))
        {searchColi();}else{
        FillListColisToScanSearch fillListColisToScanSearch = new FillListColisToScanSearch();
        fillListColisToScanSearch.execute("");}


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if (view.getId() == R.id.btnScanprev) {
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

    public class FillListColisCreated extends AsyncTask<String, String, String> {
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
                    convertView = layoutInflater.inflate(R.layout.item_colis, null);
                    final TextView txt_colis = (TextView) convertView.findViewById(R.id.txt_colis);
                    final TextView txt_poids_max = (TextView) convertView.findViewById(R.id.txt_poids_max);
                    final TextView txt_no_command = (TextView) convertView.findViewById(R.id.txt_no_command);
                    final Button bt_add = (Button) convertView.findViewById(R.id.bt_add);

                    cr = helper.getListColisCreated();
                    if (cr.move(pos + 1)) {

                        txt_colis.setText(cr.getString(cr.getColumnIndex("NoColis")));
                        txt_poids_max.setText(cr.getString(cr.getColumnIndex("PoidsMax")));
                        txt_no_command.setText(cr.getString(cr.getColumnIndex("NoCommande")));


                    }
                    bt_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Cursor cr = helper.getListCommandPrelevementColisage();


                            if (cr.move(1)) {
                                String numDoc = cr.getString(cr.getColumnIndex("NoDoc"));
                                String TypeColis = cr.getString(cr.getColumnIndex("TypeColis"));
                                String PoidsMax = cr.getString(cr.getColumnIndex("PoidsMax"));


                                txt_no_doc.setText(numDoc);
                                txt_poids_max.setText(PoidsMax);
                                txt_no_colis.setText(txt_colis.getText().toString());
                                helper.UpdateBonCommandePrelevementColisage(new PreparationColisLigne(numDoc, TypeColis, txt_colis.getText().toString(), PoidsMax));
                                FillLigneColisToScan();

                            } else {
                                Toast.makeText(getApplicationContext(), "count" + cr.getCount(), Toast.LENGTH_SHORT).show();

                            }


                        }
                    });


                    return convertView;
                }
            };
            grid_prelevement.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                cr = helper.getListColisCreated();


                if (cr.moveToFirst()) {
                    do {
                        Log.e("cursor", cr.getString(cr.getColumnIndex("NoColis")));
                    } while (cr.moveToNext());
                }


            } catch (Exception ex) {
                z = "list" + ex.toString();

            }
            return z;
        }
    }

    public class FillListColisToScan extends AsyncTask<String, String, String> {
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

                    convertView = layoutInflater.inflate(R.layout.item_ligne_colis, null);
                    final TextView txt_noDoc = (TextView) convertView.findViewById(R.id.txt_noDoc);
                    final TextView txt_poids = (TextView) convertView.findViewById(R.id.txt_poids);
                    final TextView txt_no_colis = (TextView) convertView.findViewById(R.id.txt_no_colis);
                    final TextView txt_ean = (TextView) convertView.findViewById(R.id.txt_ean);
                    final TextView txt_qt = (TextView) convertView.findViewById(R.id.txt_qt);
                    final TextView txt_code_article = (TextView) convertView.findViewById(R.id.txt_code_article);
                    final EditText edt_qt_scan = (EditText) convertView.findViewById(R.id.edt_qt_scan);
                    final Button btmoin = (Button) convertView.findViewById(R.id.btmoin);
                    final Button btplus = (Button) convertView.findViewById(R.id.btplus);
                    //PRIMARY KEY,noDoc TEXT  ,Article TEXT,noColis TEXT ,Quantite INTEGER, QuantiteScan INTEGER,Piece TEXT,poidsUnite TEXT,EAN TEXT
                    cr = helper.getListLigneCommandPrelevementColisage();
                    if (cr.move(pos + 1)) {

                        txt_code_article.setText(cr.getString(cr.getColumnIndex("Article")));
                        txt_noDoc.setText(cr.getString(cr.getColumnIndex("noDoc")));
                        txt_qt.setText(cr.getString(cr.getColumnIndex("Quantite")));
                        txt_no_colis.setText(cr.getString(cr.getColumnIndex("noColis")));
                        txt_poids.setText(cr.getString(cr.getColumnIndex("poidsUnite")));
                        txt_ean.setText(cr.getString(cr.getColumnIndex("EAN")));
                        edt_qt_scan.setText(cr.getString(cr.getColumnIndex("QuantiteScan")));


                    }


                    btmoin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float qt = Float.valueOf(edt_qt_scan.getText().toString());
                            qt--;
                            if(qt<0)
                            { qt=0;}
                            edt_qt_scan.setText("" + qt);

                            helper.UpdateLigneBonCommandePrelevementColisage(new LigneColis(txt_noDoc.getText().toString(), txt_no_colis.getText().toString(), txt_code_article.getText().toString(), txt_qt.getText().toString(), txt_poids.getText().toString(), txt_ean.getText().toString(), ""+qt));


                        }
                    });
                    btplus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float qt = Float.parseFloat(edt_qt_scan.getText().toString());
                            qt++;
                            float pt = calculPoidsTotal();
                            float pu = Float.parseFloat(txt_poids.getText().toString());
                            float pr = pt + pu;
                            if (pr <= Float.parseFloat(txt_poids_max.getText().toString())) {

                                edt_qt_scan.setText("" + qt);
                                helper.UpdateLigneBonCommandePrelevementColisage(new LigneColis(txt_noDoc.getText().toString(), txt_no_colis.getText().toString(), txt_code_article.getText().toString(), txt_qt.getText().toString(), txt_poids.getText().toString(), txt_ean.getText().toString(), Float.toString(qt)));

                            } else {
                                Toast.makeText(getApplicationContext(), "POIDS MAX DÉPASSER", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    edt_qt_scan.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {

                            String s = edt_qt_scan.getText().toString();
                            if (i != KEYCODE_DEL) {
                                if (!s.equals("")) {
                                    float qt = Float.valueOf(edt_qt_scan.getText().toString());

                                    float pt = calculPoidsTotal();
                                    float pu = Float.parseFloat(txt_poids.getText().toString());
                                    float ptu = qt * pu;
                                    Cursor item = helper.getLignePrelevementColisageArticle(txt_ean.getText().toString());
                                    item.move(1);
                                    String QuantiteScan = item.getString(cr.getColumnIndex("QuantiteScan"));
                                    float poldscan = Float.parseFloat(QuantiteScan) * pu;
                                    float poids = pt + ptu - poldscan;
                                    if (poids <= Float.parseFloat(txt_poids_max.getText().toString())) {
                                        edt_qt_scan.setText("" + qt);
                                        helper.UpdateLigneBonCommandePrelevementColisage(new LigneColis(txt_noDoc.getText().toString(), txt_no_colis.getText().toString(), txt_code_article.getText().toString(), txt_qt.getText().toString(), txt_poids.getText().toString(), txt_ean.getText().toString(), ""+qt));

                                    } else {
                                        Toast.makeText(getApplicationContext(), "POIDS MAX DÉPASSER", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }
                            return false;
                        }
                    });
                    return convertView;
                }
            };
            grid_prelevement.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                cr = helper.getListLigneCommandPrelevementColisage();


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

    public class FillListColisToScanSearch extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
        ArrayList<String> list;
        Cursor cr;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            cr = helper.getLignePrelevementColisageArticle(searchScan);
            if(cr.getCount()>0) {
                cr.move(1);
                float pt = calculPoidsTotal();
                float pu = Float.parseFloat(cr.getString(cr.getColumnIndex("poidsUnite")));
                float pr = pt + pu;
                if (pr <= Float.parseFloat(txt_poids_max.getText().toString())) {


                    helper.UpdateLignePrelevementColisageByScan(searchScan);
                } else {
                    Toast.makeText(getApplicationContext(), "POIDS MAX DÉPASSER", Toast.LENGTH_SHORT).show();
                }
            }
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

                    convertView = layoutInflater.inflate(R.layout.item_ligne_colis, null);
                    final TextView txt_noDoc = (TextView) convertView.findViewById(R.id.txt_noDoc);
                    final TextView txt_poids = (TextView) convertView.findViewById(R.id.txt_poids);
                    final TextView txt_no_colis = (TextView) convertView.findViewById(R.id.txt_no_colis);
                    final TextView txt_ean = (TextView) convertView.findViewById(R.id.txt_ean);
                    final TextView txt_qt = (TextView) convertView.findViewById(R.id.txt_qt);
                    final TextView txt_code_article = (TextView) convertView.findViewById(R.id.txt_code_article);
                    final EditText edt_qt_scan = (EditText) convertView.findViewById(R.id.edt_qt_scan);
                    final Button btmoin = (Button) convertView.findViewById(R.id.btmoin);
                    final Button btplus = (Button) convertView.findViewById(R.id.btplus);
                    //PRIMARY KEY,noDoc TEXT  ,Article TEXT,noColis TEXT ,Quantite INTEGER, QuantiteScan INTEGER,Piece TEXT,poidsUnite TEXT,EAN TEXT




                    cr = helper.getLignePrelevementColisageArticle(searchScan);
                    if (cr.move(pos + 1)) {

                        txt_code_article.setText(cr.getString(cr.getColumnIndex("Article")));
                        txt_noDoc.setText(cr.getString(cr.getColumnIndex("noDoc")));
                        txt_qt.setText(cr.getString(cr.getColumnIndex("Quantite")));
                        txt_no_colis.setText(cr.getString(cr.getColumnIndex("noColis")));
                        txt_poids.setText(cr.getString(cr.getColumnIndex("poidsUnite")));
                        txt_ean.setText(cr.getString(cr.getColumnIndex("EAN")));
                        edt_qt_scan.setText(cr.getString(cr.getColumnIndex("QuantiteScan")));


                    }


                    btmoin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float qt = Float.valueOf(edt_qt_scan.getText().toString());
                            qt--;
                            if(qt<0)
                            { qt=0;}
                            edt_qt_scan.setText("" + qt);
                            helper.UpdateLigneBonCommandePrelevementColisage(new LigneColis(txt_noDoc.getText().toString(), txt_no_colis.getText().toString(), txt_code_article.getText().toString(), txt_qt.getText().toString(), txt_poids.getText().toString(), txt_ean.getText().toString(), ""+qt));


                        }
                    });
                    btplus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float qt = Float.valueOf(edt_qt_scan.getText().toString());
                            qt++;

                            float pt = calculPoidsTotal();
                            float pu = Float.parseFloat(txt_poids.getText().toString());
                            float pr = pt + pu;
                            if (pr <= Float.parseFloat(txt_poids_max.getText().toString())) {
                                edt_qt_scan.setText("" + qt);
                                helper.UpdateLigneBonCommandePrelevementColisage(new LigneColis(txt_noDoc.getText().toString(), txt_no_colis.getText().toString(), txt_code_article.getText().toString(), txt_qt.getText().toString(), txt_poids.getText().toString(), txt_ean.getText().toString(), ""+qt));

                            } else {
                                Toast.makeText(getApplicationContext(), "POIDS MAX DÉPASSER", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    edt_qt_scan.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {

                            String s = edt_qt_scan.getText().toString();
                            if (i != KEYCODE_DEL) {
                                if (!s.equals("")) {
                                    float qt = Float.valueOf(edt_qt_scan.getText().toString());
                                    float pt = calculPoidsTotal();
                                    float pu = Float.parseFloat(txt_poids.getText().toString());
                                    float ptu = qt * pu;
                                    Cursor item = helper.getLignePrelevementColisageArticle(txt_ean.getText().toString());
                                    item.move(1);
                                    String QuantiteScan = item.getString(cr.getColumnIndex("QuantiteScan"));
                                    float poldscan = Float.parseFloat(QuantiteScan) * pu;
                                    float poids = pt + ptu - poldscan;
                                    if (poids <= Float.parseFloat(txt_poids_max.getText().toString())) {
                                        edt_qt_scan.setText("" + qt);
                                        helper.UpdateLigneBonCommandePrelevementColisage(new LigneColis(txt_noDoc.getText().toString(), txt_no_colis.getText().toString(), txt_code_article.getText().toString(), txt_qt.getText().toString(), txt_poids.getText().toString(), txt_ean.getText().toString(), ""+qt));

                                    } else {
                                        Toast.makeText(getApplicationContext(), "POIDS MAX DÉPASSER", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    return convertView;
                }
            };
            grid_prelevement.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                cr = helper.getLignePrelevementColisageArticle(searchScan);


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

    void CreatePrelevementColis() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = baseUrlCreatePrelevement;
            progressBar.setVisibility(View.VISIBLE);
            JSONArray arrayJson = new JSONArray();
            Cursor cr = helper.getListLigneCommandPrelevementColisage();
            if (cr.moveToFirst()) {
                do {
                    //       cv.put("Article", c.getArticle());
                    //        cv.put("Quantite", c.getQuantite());
                    //        cv.put("QuantiteScan", c.getQuantiteScan());
                    //        cv.put("noDoc", c.getNoDoc());
                    //        cv.put("Piece", c.getPiece());
                    //        cv.put("EAN", c.getEAN());
                    // "inputJson" : "[{"NoDoc":"101021","Article":"1896-S","Quantite":"8"},{"NoDoc":"101023","Article":"1896-S","Quantite":"6"}]"
                    JSONObject obj = new JSONObject().put("NoDoc", cr.getString(cr.getColumnIndex("noDoc")))
                            .put("Quantite", cr.getString(cr.getColumnIndex("QuantiteScan")))
                            .put("Article", cr.getString(cr.getColumnIndex("Article")))
                            .put("NoColis", cr.getString(cr.getColumnIndex("noColis")));

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

                            helper.DeleteListBonCommandPrelevementColisage();
                            helper.DeleteLigneBonCommandPrelevementColisage();
                            helper.DeleteLigneColisCreated();
                            helper.DeleteValideBonCommandPrelevementColisage();

                            Toast.makeText(getApplicationContext(), "colis crée avec succés", Toast.LENGTH_SHORT).show();
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

}