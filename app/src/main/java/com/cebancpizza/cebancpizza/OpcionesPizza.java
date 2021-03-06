package com.cebancpizza.cebancpizza;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class OpcionesPizza extends AppCompatActivity {

    Intent intent;
    TextView nombre, precio;
    EditText cantidad;
    RadioGroup rgMasa, rgTamanno;
    String masa = "masa fina", tamanno = "individual";
    Float precioMasa = Float.parseFloat("0.0"), precioTamanno = Float.parseFloat("0.0"), precioUnitario;
    long masaID;
    long tamanoID;
    long pizzaID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_pizza);

        intent = getIntent();
        nombre = (TextView)findViewById(R.id.textView29);
        precio = (TextView)findViewById(R.id.textView36);
        cantidad = (EditText) findViewById(R.id.editText13);
        rgMasa = (RadioGroup) findViewById(R.id.radioGroup2);
        rgTamanno = (RadioGroup) findViewById(R.id.radioGroup3);
        precioUnitario = Float.parseFloat(intent.getStringExtra("Precio"));
        nombre.setText(intent.getStringExtra("Nombre"));
        precio.setText(intent.getStringExtra("Precio"));
        pizzaID=Long.parseLong(intent.getStringExtra("id"));
        masaID =1;
        tamanoID=1;


        rgMasa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radioButton) {
                    masa = "masa fina";
                    masaID =1;
                    precioMasa = Float.parseFloat("0.00");
                } else if (checkedId == R.id.radioButton2) {
                    masa = "masa normal";
                    masaID = 2;
                    precioMasa = Float.parseFloat("1.00");
                }
                calcularPrecio();
            }
        });

        rgTamanno.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radioButton3) {
                    tamanno = "individual";
                    tamanoID=1;
                    precioTamanno = Float.parseFloat("0.00");
                } else if (checkedId == R.id.radioButton5) {
                    tamanno = "mediana";
                    tamanoID=2;
                    precioTamanno = Float.parseFloat("2.00");
                } else if (checkedId == R.id.radioButton6) {
                    tamanno = "familiar";
                    tamanoID=3;
                    precioTamanno = Float.parseFloat("4.00");
                }
                calcularPrecio();
            }
        });
    }

    public void masUno(View v) {
        int iCant;
        iCant = Integer.parseInt(cantidad.getText().toString());
        iCant++;
        cantidad.setText(String.valueOf(iCant));
        calcularPrecio();
    }

    public void menosUno(View v) {
        int iCant;
        iCant = Integer.parseInt(cantidad.getText().toString());
        iCant--;
        if (iCant<0){
            iCant=0;
        }
        cantidad.setText(String.valueOf(iCant));
        calcularPrecio();
    }

    public void calcularPrecio() {
        double precioTotal;
        precioUnitario = Float.parseFloat(intent.getStringExtra("Precio")) + precioMasa + precioTamanno;
        precioTotal = precioUnitario * Integer.parseInt(cantidad.getText().toString());
        precio.setText(Double.toString(precioTotal));
    }

    public void anadirPizzaPedido(View v){
        Pizza pizza = new Pizza(nombre.getText().toString(),masa,tamanno,precioUnitario,Integer.parseInt(cantidad.getText().toString()));
        pizza.setMasaId(masaID);
        pizza.setTamanoId(tamanoID);
        pizza.setId(pizzaID);
        ((Pedido) this.getApplication()).anadirPizza(pizza);
        finish();
    }
    public void volver(View v){
        finish();
    }
}