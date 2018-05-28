package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.dispArray;
import static com.quadx.dungeons.states.mapstate.MapState.rn;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 5/13/2018.
 */
public class CamController {
    private float force = 0;
    private boolean shakeCam = false;
    private boolean snapCam = false;

    private Delta dLockTime = new Delta( 15*60*ft);

    private Delta dShake;

    public CamController() {
        dShake = new Delta(0 * ft);

    }

    public void shakeScreen(float time, float f) {
        dShake = new Delta(time * ft);
        force = f;
        shakeCam = true;
    }

    private Vector3 camDisplacement() {
        if (shakeCam) {
            float x = (float) (force * rn.nextGaussian()),
                    y = (float) (force * rn.nextGaussian());
            return new Vector3(x, y, 0);
        } else
            return new Vector3();
    }

    private void updateCamPos(OrthographicCamera cam) {
        Vector3 position = cam.position;
        float[] f = dispArray(player.pos()).getCorners().getVertices();
        Vector3 disp = new Vector3(f[8], f[9], 0);
        if (snapCam) {
            Vector2 p=player.fixed();
            position.set(p.x,p.y, 0);
            snapCam = false;
        } else {
            disp.add(camDisplacement());
            float alpha = isShaking() ? force/12f : .2f;
            position.lerp(disp, alpha);
        }

        cam.position.set(position);
        cam.update();
        float x = cam.position.x - cam.viewportWidth / 2;
        float y = cam.position.y - cam.viewportHeight / 2;
        State.setView(new Vector2(x, y));
    }

    public void update(float dt, OrthographicCamera cam) {
        if (shakeCam) {
            dShake.update(dt);
            dLockTime.update(dt);
        }
        if (dShake.isDone()) {
            shakeCam = false;
        }
        if(dLockTime.isDone()){
            shakeCam=false;
            snapCam=true;
            dLockTime.reset();
        }

        updateCamPos(cam);
    }

    private boolean isShaking() {
        return shakeCam;
    }

    public void setSnapCam(boolean snapCam) {
        this.snapCam = snapCam;
    }
}
