package com.devbaltasarq.listadelacompra.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.devbaltasarq.listadelacompra.R;
import com.devbaltasarq.listadelacompra.core.Item;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter {
    public ItemArrayAdapter(Context context, ArrayList<Item> items)
    {
        super( context, 0, items );
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        final Context CONTEXT = this.getContext();
        final LayoutInflater INFLATER = LayoutInflater.from( CONTEXT );
        final Item ITEM = (Item) this.getItem( position );

        // Crear la vista si no existe
        if ( view == null ) {
            view = INFLATER.inflate( R.layout.listview_item, null );
        }

        // Rellenar los datos
        final TextView lblNombre = view.findViewById( R.id.lblNombre );
        final TextView lblSupermercado = view.findViewById( R.id.lblSupermercado );

        lblNombre.setText( ITEM.getNombre() );
        lblSupermercado.setText( ITEM.getSupermercado() );

        return view;
    }
}
