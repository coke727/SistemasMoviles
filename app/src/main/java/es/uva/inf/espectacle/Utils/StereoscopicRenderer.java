package es.uva.inf.espectacle.utils;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;

import org.rajawali3d.cardboard.RajawaliCardboardRenderer;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

import es.uva.inf.espectacle.R;

/**
 * Created by Rober on 01/01/2016.
 */
public class StereoscopicRenderer extends RajawaliCardboardRenderer{
    Context mContext;

    private MediaPlayer mMediaPlayer;
    private StreamingTexture mVideoTexture;

    public StereoscopicRenderer(Context context) {
        super(context);
        mContext = context;
    }



    @Override
    protected void initScene() {


        mMediaPlayer = MediaPlayer.create(getContext(),
                R.raw.sintel_trailer_480p);
        mMediaPlayer.setLooping(true);

        mVideoTexture = new StreamingTexture("video", mMediaPlayer);
        //mMediaPlayer.prepareAsync();    //prepare the player (asynchronous)
        /*mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start(); //start the player only when it is prepared
            }
        });*/

        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            throw new RuntimeException(e);
        }

        Sphere sphere = new Sphere(50, 64, 32);
        sphere.setScaleX(-1);
        sphere.setMaterial(material);

        getCurrentScene().addChild(sphere);

        getCurrentCamera().setPosition(Vector3.ZERO);

        getCurrentCamera().setFieldOfView(75);

        //mMediaPlayer.seekTo(savePos);

        mMediaPlayer.start();

        /*PointLight pointLight = new PointLight();
        pointLight.setPower(1);
        pointLight.setPosition(-1, 1, 4);

        getCurrentScene().addLight(pointLight);
        getCurrentScene().setBackgroundColor(0xff040404);

        try {
            Object3D android = new Cube(2.0f);
            Material material = new Material();
            material.enableLighting(true);
            material.setDiffuseMethod(new DiffuseMethod.Lambert());
            material.setSpecularMethod(new SpecularMethod.Phong());
            android.setMaterial(material);
            android.setColor(0xff99C224);
            getCurrentScene().addChild(android);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        mMediaPlayer = MediaPlayer.create(getContext(),
                R.raw.fredy);
        mMediaPlayer.setLooping(true);

        mVideoTexture = new StreamingTexture("sintelTrailer", mMediaPlayer);
        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        Plane screen = new Plane(3, 2, 2, 2, Vector3.Axis.Z);
        screen.setMaterial(material);
        screen.setRotY(180);
        screen.setX(.1f);
        screen.setY(-.2f);
        screen.setZ(1.5f);
        getCurrentScene().addChild(screen);

        getCurrentCamera().enableLookAt();
        getCurrentCamera().setLookAt(0, 0, 0);

        // -- animate the spot light

        TranslateAnimation3D lightAnim = new TranslateAnimation3D(
                new Vector3(-3, 3, 10), // from
                new Vector3(3, 1, 3)); // to
        lightAnim.setDurationMilliseconds(5000);
        lightAnim.setRepeatMode(Animation.RepeatMode.REVERSE_INFINITE);
        lightAnim.setTransformable3D(pointLight);
        lightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        getCurrentScene().registerAnimation(lightAnim);
        lightAnim.play();

        // -- animate the camera

        EllipticalOrbitAnimation3D camAnim = new EllipticalOrbitAnimation3D(
                new Vector3(3, 2, 10), new Vector3(1, 0, 8), 0, 359);
        camAnim.setDurationMilliseconds(20000);
        camAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        camAnim.setTransformable3D(getCurrentCamera());
        getCurrentScene().registerAnimation(camAnim);
        camAnim.play();

        mMediaPlayer.start();*/

    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        mVideoTexture.update();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    @Override
    public void onRenderSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        super.onRenderSurfaceDestroyed(surfaceTexture);
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}

