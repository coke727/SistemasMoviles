package es.uva.inf.espectacle.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.uva.inf.espectacle.R;

/**
 * MediaHolder de los elemmentos multimedia de las listas, establecemos los datos
 * para que se muestren en la lista
 */
public class MediaHolder extends RecyclerView.ViewHolder{
    final TextView title;
    final TextView subtitle;
    final TextView duration;
    final ImageView imagen;

    /**
     * Constructor del MediaHolder
     * @param itemView item de la lista.
     */
    MediaHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        subtitle = (TextView)itemView.findViewById(R.id.subtitle);
        duration = (TextView)itemView.findViewById(R.id.duration);
        imagen = (ImageView)itemView.findViewById(R.id.imagen);
    }
}
