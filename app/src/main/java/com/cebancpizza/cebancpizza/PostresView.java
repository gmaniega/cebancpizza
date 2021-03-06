package com.cebancpizza.cebancpizza;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class PostresView extends AppCompatActivity {
    EditText cantidad;
    FeedReaderDbHelper conexion = null;
    ArrayList<ProductoModel> dataSet = new  ArrayList<>();
    ListView listaPostres ;
    ProductoModel productoActual;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postres_view);
        cantidad = (EditText) findViewById(R.id.textCantidadPostres);
        cantidad.setText("0");
        listaPostres = (ListView) findViewById(R.id.listViewPostres);
        cargarView();

    }

    public void cargarView(){

        try {
            conexion = new FeedReaderDbHelper(getApplicationContext());

            SQLiteDatabase db = conexion.getWritableDatabase();

            String[] projection = {
                    TablasBBDD.TablaProducto.COLUMN_NOMBRE,
                    TablasBBDD.TablaProducto.COLUMN_TIPO_PRODUCTO,
                    TablasBBDD.TablaProducto.COLUMN_PRECIO,
                    TablasBBDD.TablaProducto.COLUMN_ID,
                    TablasBBDD.TablaProducto.COLUMN_IMAGEN
            };

            String selection = TablasBBDD.TablaProducto.COLUMN_TIPO_PRODUCTO+ " = ?";
            //String[] selectionArgs = { nombreText.getText().toString() };
            String[] selectionArgs = { "Postre" };

            Cursor cursor = db.query(
                    TablasBBDD.TablaProducto.TABLE_NAME,       // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                      // The sort order
            );

            while(cursor.moveToNext()) {
                ProductoModel p = new ProductoModel();

                p.setNombre(cursor.getString(cursor.getColumnIndex(TablasBBDD.TablaProducto.COLUMN_NOMBRE)));
                p.setDescripcion(" ");
                p.setImagen(cursor.getString(cursor.getColumnIndex(TablasBBDD.TablaProducto.COLUMN_IMAGEN)));
                p.setId(cursor.getLong(cursor.getColumnIndex(TablasBBDD.TablaProducto.COLUMN_ID)));
                p.setPrecio(cursor.getFloat(cursor.getColumnIndex(TablasBBDD.TablaProducto.COLUMN_PRECIO)));
//                Toast.makeText(getBaseContext(), p.nombre, Toast.LENGTH_LONG).show();
                dataSet.add(p);

            }


            ProductosAdapter adapter = new ProductosAdapter(dataSet, getApplicationContext());
            listaPostres.setAdapter(adapter);


            listaPostres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    productoActual= dataSet.get(position);
                    obtenerCantidadPostres();
//                    Toast.makeText(getBaseContext(), producto.nombre, Toast.LENGTH_LONG).show();

                }
            });

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void obtenerCantidadPostres(){
        cantidad.setText(String.valueOf(((Pedido) this.getApplication()).getCantidadPostre(productoActual.getId())));
//        Toast.makeText(this, productoActual.getNombre()+" "+((Pedido) this.getApplication()).getCantidadBebida(productoActual.getId()), Toast.LENGTH_SHORT).show();
    }



    public void anadirPostrePedido(View v){
//        Toast.makeText(this, productoActual.getNombre(), Toast.LENGTH_SHORT).show();
        try {
            int cantidadPostres = Integer.parseInt(cantidad.getText().toString());
            Postre postre = new Postre(productoActual.getNombre(), cantidadPostres, productoActual.getPrecio());
            postre.setId(productoActual.getId());
            ((Pedido) this.getApplication()).anadirPostre(postre);
            Toast.makeText(this, productoActual.getNombre() + " en pedido: " + cantidadPostres, Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(this, "Elije un postre primero", Toast.LENGTH_SHORT).show();
        }
    }


    public void volver(View v){
        finish();
    }

    public void siguiente(View v) {
        Intent intent = new Intent(this, RevisarPedido.class);
        startActivity(intent);
    }
    public void revisarPedido(View v) {
        Intent intent = new Intent(this, RevisarPedido.class);
        startActivity(intent);
    }

    public void masUno(View v) {

        int iCant;
        iCant = Integer.parseInt(cantidad.getText().toString());
        iCant++;
        cantidad.setText(String.valueOf(iCant));
    }

    public void menosUno(View v) {
        int iCant;
        iCant = Integer.parseInt(cantidad.getText().toString());
        iCant--;
        if (iCant<0){
            iCant=0;
        }
        cantidad.setText(String.valueOf(iCant));
    }
}
