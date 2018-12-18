package ar.libgdx;

import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;

import com.badlogic.gdx.input.GestureDetector;
import ar.vuforia.VuforiaRenderer;

/**
 * Instance of libgdx Game class responsible for rendering 3D content over augmented reality.
 */
public class Engine extends Game {

    private FPSLogger fps;
    private VuforiaRenderer vuforiaRenderer;
    private String objName;
    private  String texName;
    private  String soundName;
    private Display mDisplay;

    public Engine(VuforiaRenderer vuforiaRenderer, String objName, String texName, String soundName) {
        this.vuforiaRenderer = vuforiaRenderer;
        this.objName = objName;
        this.texName = texName;
        this.soundName = soundName;
    }

    @Override
    public void create () {

        mDisplay = new Display(vuforiaRenderer, objName, texName, soundName);
        setScreen(mDisplay);
        vuforiaRenderer.initRendering();
        fps = new FPSLogger();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Log.d("ENGINE", "Resize: "+width+"x"+height);
        vuforiaRenderer.onSurfaceChanged(width, height);
    }

    @Override
    public void render () {
        super.render();
        fps.log();


    }

}
