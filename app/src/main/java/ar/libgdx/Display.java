package ar.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;
import ar.vuforia.VuforiaRenderer;

/**
 * Screen implementation responsible for model loading and calling renderer properly.
 */
public class Display implements Screen, InputProcessor {

    public ModelInstance modelInstance;
    public Model model;

    public Renderer mRenderer;
    private TextureProvider tp;
    private AnimationController an;


    private Vector3 position;
    public Vector3 center;
    public Vector3 dimensions;
    public float radius;
    private AnimListener animListener;

    private Sound sound;

    private final static BoundingBox bounds = new BoundingBox();



    public Display(VuforiaRenderer vuforiaRenderer, String objName, String texName, String soundName) {

        mRenderer = new Renderer(vuforiaRenderer);

        /*AssetManager assets = new AssetManager();
        assets.load("jet.g3db", Model.class);
        assets.finishLoading();*/

        UBJsonReader jsonReader = new UBJsonReader(); //Reader for model g3d

        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        tp = new TextureProvider() { //tecture loader, load from internal storage
            @Override
            public Texture load(String fileName) {
                Texture result = new Texture(Gdx.files.local(fileName));
                result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                result.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                return result;
            }
        };

        texName = texName.replace('_', ' ');
        tp.load(texName);
        Gdx.app.log("gdx ", texName);
        Gdx.app.log("gdx ", soundName);
        //model = assets.get("jet.g3db", Model.class);
        if(soundName != "")
            sound = Gdx.audio.newSound(Gdx.files.local(soundName));

        model = modelLoader.loadModel(Gdx.files.local(objName), tp); //load model

        modelInstance = new ModelInstance(model);
        updateBB();

        an = new AnimationController(modelInstance);
        an.allowSameAnimation = true;
        Array<com.badlogic.gdx.graphics.g3d.model.Animation> ar  =  modelInstance.animations;

        Gdx.app.log("Gdx : ", ""+ar.size);

        Gdx.input.setInputProcessor(this);
        animListener = new AnimListener(an);

    }

    public void updateBB()
    {
        center = new Vector3();
        dimensions = new Vector3();
        position = new Vector3();

        modelInstance.calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
    }

    public void playAnim()
    {
        if(!an.inAction)
        {
            Gdx.app.log("sound ", sound.toString());
            if(sound != null)
            {
                sound.play(1.0f);
            }
            for (com.badlogic.gdx.graphics.g3d.model.Animation anim : modelInstance.animations)
            {
                an.setAnimation(anim.id, animListener);
            }
        }

    }

    public void resetAnim()
    {
        for (com.badlogic.gdx.graphics.g3d.model.Animation anim : modelInstance.animations)
        {
            updateBB();
        }
    }

    public boolean clickObject(int screenX, int screenY)
    {
        boolean isClick = false;
        Ray ray = mRenderer.camera.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;

        modelInstance.transform.getTranslation(position);
        position.add(center);
        float dist2 = ray.origin.dst2(position);
        if (distance >= 0f && dist2 > distance)
        {

        }
        if (Intersector.intersectRaySphere(ray, position, radius, null)) {
            distance = dist2;
            isClick = true;
        }

        return isClick;
    }



    @Override
    public void render(float delta) {
        mRenderer.render(this, delta);

        if(an != null)
            an.update(Gdx.graphics.getDeltaTime());

    }

    @Override
    public void dispose() {
        mRenderer.dispose();
    }


    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {

        boolean isClick = clickObject(screenX, screenY);
        if(isClick)
            playAnim();

        return true;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {



        return true;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("gdx", "touch up:");
        return false;
    }



    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public class AnimListener implements AnimationController.AnimationListener
    {
        private AnimationController an;

        public AnimListener(AnimationController animationController)
        {
            this.an = animationController;
        }
        @Override
        public void onEnd(AnimationController.AnimationDesc animation) {
            resetAnim();
        }


        @Override
        public void onLoop(AnimationController.AnimationDesc animation) {

        }
    }
}
