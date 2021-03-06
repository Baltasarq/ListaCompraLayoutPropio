package com.devbaltasarq.listadelacompra.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.devbaltasarq.listadelacompra.R;
import com.devbaltasarq.listadelacompra.core.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static String LogTag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btNuevo = this.findViewById( R.id.btNuevo );
        final ListView lvItems = this.findViewById( R.id.lvItems );

        this.items = new ArrayList<>();
        btNuevo.setOnClickListener( (v) -> creaNuevoElemento() );
        this.registerForContextMenu( lvItems );
        this.creaLista();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Guardar la lista de la compra
        Log.d( LogTag, "Guardando la lista de la compra..." );

        final SharedPreferences prefs = this.getPreferences( MODE_PRIVATE );
        final SharedPreferences.Editor editor = prefs.edit();
        Set<String> compras = new HashSet<>();

        for(Item item: this.items) {
            final String LINEA_ITEM = item.getNombre() + "," + item.getSupermercado();

            Log.d( LogTag, "Guardado: '" + LINEA_ITEM + "'" );
            compras.add( LINEA_ITEM );
        }

        editor.putStringSet( "compras", compras );
        editor.apply();
        Log.d( LogTag, "Lista de la compra guardada." );
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Recuperar la lista de la compra
        Log.d( LogTag, "Recuperando lista de la compra..." );

        final SharedPreferences prefs = this.getPreferences( MODE_PRIVATE );
        Set<String> compras = prefs.getStringSet( "compras",
                                                 new HashSet<>() );

        this.items.clear();
        for(String item: compras) {
            final String[] PARTES = item.trim().split( "," );
            String supermercado = "";

            if ( PARTES.length > 1 ) {
                supermercado = PARTES[ 1 ];
            }

            Log.d( LogTag, "Recuperado: '" + item + "'" );
            this.items.add( new Item( PARTES[ 0 ], supermercado ) );
        }

        this.creaLista();
        Log.d( LogTag, "Lista de la compra recuperada." );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        this.getMenuInflater().inflate( R.menu.main_menu, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean toret = false;
        super.onOptionsItemSelected(item);

        switch( item.getItemId() ) {
            case R.id.opSalir:
                this.finish();
                toret = true;
                break;
            case R.id.opNuevo:
                this.creaNuevoElemento();
                toret = true;
                break;
        }

        return toret;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu( menu, v, menuInfo );

        if ( v.getId() == R.id.lvItems ) {
            this.getMenuInflater().inflate( R.menu.context_menu, menu );
        }

        return;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        boolean toret = false;
        super.onContextItemSelected( item );

        if ( item.getItemId() == R.id.opElimina ) {
            toret = true;
            int pos = ( (AdapterView.AdapterContextMenuInfo)
                    item.getMenuInfo() ).position;
            this.eliminaItem( pos );
        }

        return toret;
    }

    private void creaLista()
    {
        final ListView lvItems = this.findViewById( R.id.lvItems );

        this.adapterList = new ItemArrayAdapter( this, this.items );
        lvItems.setAdapter( this.adapterList );
    }

    private void creaNuevoElemento()
    {
        final AlertDialog.Builder dlg = new AlertDialog.Builder( this );
        final EditText edName = new EditText( this );

        dlg.setTitle( R.string.nuevo );
        dlg.setView( edName );
        dlg.setNegativeButton( R.string.cancelar, null );
        dlg.setPositiveButton( R.string.guardar, (d, i) -> {
            final String CONTENIDO = edName.getText().toString().trim();
            String[] datos = CONTENIDO.split( " " );
            Item item = null;

            // Siguiendo el num. de partes en la cadena...
            if ( datos.length > 1 ) {
                item = new Item( datos[ 0 ], datos[ 1 ] );
            }
            else
            if ( datos.length == 1 ) {
                if ( !CONTENIDO.isEmpty() ) {
                    item = new Item( datos[ 0 ], "" );
                } else {
                    Toast.makeText( this, "Sin datos", Toast.LENGTH_SHORT ).show();
                }
            }

            // Insertar el item
            if ( item != null ) {
                this.adapterList.add( item );
            }

            return;
        });

        dlg.create().show();
    }

    private void eliminaItem(int pos)
    {
        this.items.remove( pos );
        this.adapterList.notifyDataSetChanged();
    }

    private ItemArrayAdapter adapterList;
    private ArrayList<Item> items;
}
