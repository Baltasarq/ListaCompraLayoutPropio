package com.devbaltasarq.listadelacompra.ui;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.devbaltasarq.listadelacompra.R;
import com.devbaltasarq.listadelacompra.core.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btNuevo = this.findViewById( R.id.btNuevo );
        final ListView lvItems = this.findViewById( R.id.lvItems );

        this.items = new ArrayList<>();
        btNuevo.setOnClickListener( (v) -> creaNuevoElemento() );
        lvItems.setOnItemLongClickListener( (lv, iv, pos, id) -> {
            this.eliminaItem( pos );
            return true;
        });
        this.creaLista();
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
