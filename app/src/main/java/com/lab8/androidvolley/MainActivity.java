package com.lab8.androidvolley;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lab8.androidvolley.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    private final String baseUrl = "https://658d0a757c48dce947386405.mockapi.io";
    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        getProductsRequest();
    }

    private void getProductsRequest() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = baseUrl + "/products";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);
                System.out.println(response);

                try {
                    // Dữ liệu trả về là Array Object -> Dùng JSONArray để lưu data
                    JSONArray data = new JSONArray(response);
                    for (int i = 0; i < data.length(); i++) {
                        // data.getJSONObject(i) -> Lấy ra các product trong array
                        JSONObject productObject = data.getJSONObject(i);

                        Product product = new Product();

                        // Convert về đúng kiểu dữ liệu và gán vào product
                        product.id = productObject.getInt("id");
                        product.name = productObject.getString("name");
                        product.price = productObject.getDouble("price");

                        // Thêm vào danh sách products
                        products.add(product);
                    }

                    Log.d("productLength", String.valueOf(products.size()));
                    renderProduct();
                } catch (Exception e) {
                    Log.e("api", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("apiError", error.getMessage());

            }
        });

        queue.add(request);
    }

    private void addProductWithStringRequest() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = baseUrl + "/products";
        Product product = Product.generateFakeProduct();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        getProductsRequest();
                        Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }, error -> {
            Toast.makeText(getApplicationContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id", String.valueOf(product.getId()));
                param.put("name", product.getName());
                param.put("price", String.valueOf(product.getPrice()));

                return param;
            }
        };


        queue.add(request);

    }

    private void addProductWithJsonObjectRequest() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = baseUrl + "/products";
        Product product = Product.generateFakeProduct();


        // Using Gson to convert (object to json)
        String productJson = new Gson().toJson(product);
        JSONObject productJsonData = new JSONObject(productJson);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, productJsonData,
                response -> {
                    getProductsRequest(); // render new data
                    Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                }, error -> {
            Toast.makeText(getApplicationContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
        });

        queue.add(request);
    }

    private void deleteProduct(int productId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = baseUrl + "/products/" + productId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();

                    getProductsRequest();
                }, error -> {
            Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
        });

        // Thêm yêu cầu vào hàng đợi
        queue.add(request);
    }

    private void editProductRequest(Product product) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Đường dẫn URL của yêu cầu
        String url = baseUrl + "/products/" + product.getId();

        String productJson = new Gson().toJson(product);
        JSONObject productJsonData = new JSONObject(productJson);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, productJsonData, response -> {
            getProductsRequest();
            Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();

        }, error -> {
            Toast.makeText(getApplicationContext(), "Sửa thất bại", Toast.LENGTH_SHORT).show();

        });

        queue.add(request);
    }


    private void renderProduct() {
        String data = "123";

        for (Product product : products) {
            data += product.name + " " + String.valueOf(product.id) + " " + String.valueOf(product.price) + "\n";
        }

        Log.i("data", data);

        textView.setText(data);
    }
}