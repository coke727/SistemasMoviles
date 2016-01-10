package es.uva.inf.espectacle.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;

import es.uva.inf.espectacle.fragments.ImageListFragment;
import es.uva.inf.espectacle.modelo.Imagen;
import es.uva.inf.espectacle.R;

/**
 * Clase que modela el adaptador para la lista de imagenes
 */
public class ImageAdapter extends RecyclerView.Adapter<MediaHolder> implements Comparator{
    private ArrayList<Imagen> datos = new ArrayList<>();
    private Context context; //TODO meterlo con un bundle en el intent
    private ImageListFragment fragment;
    private int pos_seleccionado;
    private MediaHolder seleccionado;

    public ImageAdapter(ImageListFragment fragment){
        this.fragment = fragment;
        this.pos_seleccionado = -1;
    }

    @Override
    public MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MediaHolder(view);
    }

    @Override
    public void onBindViewHolder(final MediaHolder holder, final int position) {
        holder.title.setText(getDatos().get(position).getDisplay_name());
        holder.subtitle.setText(getDatos().get(position).getDateAdded());
        //holder.subtitle.setVisibility(View.GONE); //Escondemos el subtitulo ya que en el video no nos interesa.
        holder.duration.setText(getDatos().get(position).getSize(context));
        holder.imagen.setImageBitmap(getDatos().get(position).getThumbnail());

        if(getPos_seleccionado() == holder.getAdapterPosition() ) {
            holder.itemView.findViewById(R.id.item_texts).setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        } else {
            holder.itemView.findViewById(R.id.item_texts).setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundLight));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos_anterior = getPos_seleccionado();
                MediaHolder anterior = getSeleccionado();

                setPos_seleccionado(holder.getAdapterPosition());
                setSeleccionado(holder);
                //Log.d("espectacle", Integer.toString(getPos_seleccionado()));
                v.findViewById(R.id.item_texts).setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
                if(( anterior != null) && (anterior != holder)) {
                    anterior.itemView.findViewById(R.id.item_texts).setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundLight));
                }
                fragment.getmListener().setMedia(getDatos().get(position));
            }
        });
    }

    public void setSeleccionado (MediaHolder seleccionado) {
        this.seleccionado = seleccionado;
    }
    public MediaHolder getSeleccionado () {
        return this.seleccionado;
    }
    public void setPos_seleccionado (int pos) {
        this.pos_seleccionado = pos;
        //Log.d("espectacle", Integer.toString(getPos_seleccionado()));
    }
    public int getPos_seleccionado () {
        return this.pos_seleccionado;
    }

    @Override
    public int getItemCount() {
        return getDatos().size();
    }

    /**
     * Retorna los datos de imagen en forma de ArrayList
     * @return ArrayList con los datos de imagen
     */
    public ArrayList<Imagen> getDatos() {
        return datos;
    }

    /**
     * Establece los datos de imagen en forma de ArrayList
     * @param datos Los datos de imagen
     */
    public void setDatos(ArrayList<Imagen> datos) {
        this.datos = datos;
        this.notifyDataSetChanged();
    }

    /**
     * Establece el contexto de la aplicacion
     * @param context Contexto de la aplicacion
     */
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        return 0;
    }
}
