package es.uva.inf.espectacle.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

import es.uva.inf.espectacle.interfaces.ComunicationListener;
import es.uva.inf.espectacle.modelo.Video;
import es.uva.inf.espectacle.R;
/**
 * Clase que modela el fragment del reproductor de video
 */
public class VideoPlayerFragment extends Fragment implements View.OnClickListener {

    private boolean pause = false;
    private String path;
    private int savePos = 0;
    private ComunicationListener mListener;
    private ArrayList<Video> videoList;
    private VideoView video;
    private int numVideo = 0;

    public VideoPlayerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            videoList = new ArrayList<>(Video.getAllVideos(getContext()));
            path = videoList.get(0).getPath();
            Log.d("OnCreateFragment:", "Arguments==null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        MediaController mediaController = new MediaController(this.getActivity());
        video = (VideoView) view.findViewById(R.id.surfaceView);
        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        video.setMinimumWidth(width);
        video.setMinimumHeight(height);
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);
        //mediaController.show();
        video.setVideoPath(path);
        return view;
    }
    /**
     * Handler para el boton de reproducir video
     */
    private void onPlayButton() {
        try {
            if(pause){
                video.seekTo(savePos);
                video.pause();
            }else{
                savePos = video.getCurrentPosition();
                video.requestFocus();
                video.start();
            }
        } catch (Exception e) {
            Log.d("ERROR" , e.getMessage());
        }
        pause = !pause;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /**
     * Handler para el click en un boton del reproductor
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mListener = (ComunicationListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        if (video != null) {
            savePos = video.getCurrentPosition();
            guardarEstado.putString("ruta", path);
            guardarEstado.putInt("posicion", savePos);
        }
    }

    @Override
    public void onActivityCreated(Bundle guardarEstado) {
        super.onActivityCreated(guardarEstado);
        if (guardarEstado != null) {
            // Restore last state for checked position.
            path = guardarEstado.getString("ruta");
            savePos = guardarEstado.getInt("posicion");
        }

    }

    @Override
    public void onClick(View v) {
        /*if(v.getId()==R.id.buttonPlay){
            onPlayButton();
        }else if(v.getId()==R.id.buttonNext){
            onNextButton();
        }else if(v.getId()==R.id.buttonBack){
            onBackButton();
        }*/
    }

    /**
     * Handler para el boton de anterior del reproductor de video
     */
    private void onBackButton() {
        path = videoList.get(numVideo-1).getPath();
        numVideo--;
        video.setVideoPath(path);
        video.start();
    }

    /**
     * Handler para el boton de siguiente del reproductor de video
     */
    private void onNextButton() {
        path = videoList.get(numVideo+1).getPath();
        numVideo++;
        video.setVideoPath(path);
        video.start();
    }

}
