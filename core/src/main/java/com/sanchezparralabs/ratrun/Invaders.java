package com.sanchezparralabs.ratrun;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.Array;
import com.sanchezparralabs.ratrun.screens.GameLoop;
import com.sanchezparralabs.ratrun.screens.GameOver;
import com.sanchezparralabs.ratrun.screens.InvadersScreen;
import com.sanchezparralabs.ratrun.screens.MainMenu;

import java.lang.reflect.InvocationTargetException;


///** {@link com.badlogic.gdx.Game} implementation shared by all platforms. */
public class Invaders extends Game {

    /** Music needs to be a class property to prevent being disposed. */
    private Music music;
    private FPSLogger fps;

    private Controller controller;
    private final ControllerAdapter controllerListener = new ControllerAdapter(){
        @Override
        public void connected(Controller c) {
            if (controller == null) {
                controller = c;
            }
        }
        @Override
        public void disconnected(Controller c) {
            if (controller == c) {
                controller = null;
            }
        }
    };

    public Controller getController() {
        return controller;
    }

    @Override
    public void render () {
        InvadersScreen currentScreen = getScreen();

        // update the screen
        currentScreen.render(Gdx.graphics.getDeltaTime());

        // When the screen is done we change to the
        // next screen. Ideally the screen transitions are handled
        // in the screen itself or in a proper state machine.
        if (currentScreen.isDone()) {
            // dispose the resources of the current screen
            currentScreen.dispose();

            // if the current screen is a main menu screen we switch to
            // the game loop
            if (currentScreen instanceof MainMenu) {
                var next = ((MainMenu) currentScreen).getNext();
                if (next != null) {
                    try {
                        setScreen((Screen) next.getConstructor(Invaders.class).newInstance(this));
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
//                setScreen(new GameLoop(this));
            } else {
                // if the current screen is a game loop screen we switch to the
                // game over screen
                if (currentScreen instanceof GameLoop) {
                    setScreen(new GameOver(this));
                } else if (currentScreen instanceof GameOver) {
                    // if the current screen is a game over screen we switch to the
                    // main menu screen
                    setScreen(new MainMenu(this));
                }
            }
        }

//         fps.log();
    }

    @Override
    public void create () {
        Array<Controller> controllers = Controllers.getControllers();
        if (controllers.size > 0) {
            controller = controllers.first();
        }
        Controllers.addListener(controllerListener);

        setScreen(new MainMenu(this));
        music = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/8.12.mp3", FileType.Internal));
        music.setLooping(true);
        music.play();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp (int keycode) {
                if (keycode == Keys.ENTER && Gdx.app.getType() == ApplicationType.WebGL) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
                return true;
            }
        });

        fps = new FPSLogger();
    }

    /** For this game each of our screens is an instance of InvadersScreen.
     * @return the currently active {@link InvadersScreen}. */
    @Override
    public InvadersScreen getScreen () {
        return (InvadersScreen)super.getScreen();
    }
}
