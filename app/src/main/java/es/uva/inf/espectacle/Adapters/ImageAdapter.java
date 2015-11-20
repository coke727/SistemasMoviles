package es.uva.inf.espectacle.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;

import es.uva.inf.espectacle.Modelo.Audio;
import es.uva.inf.espectacle.Modelo.Imagen;
import es.uva.inf.espectacle.R;

public class ImageAdapter extends RecyclerView.Adapter<MediaHolder> implements Comparator{

    private ArrayList<Imagen> datos = new ArrayList<>();
    private Context context; //TODO meterlo con un bundle en el intent;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO conseguir obtener la posición del elemento clickado
            Log.d("espectacle", "Seleccionado elemento de la lista: " + getDatos().toString());
        }
    };

    @Override
    public MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new MediaHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaHolder holder, int position) {
        holder.title.setText(getDatos().get(position).getDisplay_name());
        holder.subtitle.setText(getDatos().get(position).getDateAdded());
        //holder.subtitle.setVisibility(View.GONE); //Escondemos el subtitulo ya que en el video no nos interesa.
        holder.duration.setText(getDatos().get(position).getSize(context));
        holder.imagen.setImageBitmap(getDatos().get(position).getThumbnail());
    }

    @Override
    public int getItemCount() {
        return getDatos().size();
    }

    public ArrayList<Imagen> getDatos() {
        return datos;
    }

    public void setDatos(ArrayList<Imagen> datos) {
        this.datos = datos;
        this.notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        return 0;
    }
}
