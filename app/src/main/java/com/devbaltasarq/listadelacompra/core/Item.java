package com.devbaltasarq.listadelacompra.core;

public class Item {
    public Item(String n, String s)
    {
        this.nombre = n;
        this.supermercado = s;
    }

    public String getNombre()
    {
        return this.nombre;
    }

    public String getSupermercado()
    {
        return this.supermercado;
    }

    public String toString()
    {
        final String SUPERMERCADO = this.getSupermercado();
        String toret;

        if ( SUPERMERCADO != null
          && !SUPERMERCADO.isEmpty() )
        {
            toret = String.format( "%s (%s)", this.getNombre(), this.getSupermercado() );
        } else {
            toret = this.getNombre();
        }

        return toret;
    }

    private String nombre;
    private String supermercado;
}
