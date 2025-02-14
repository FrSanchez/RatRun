package com.sanchezparralabs.ratrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TestShader implements Shader {
    ShaderProgram program;
    Camera camera;
    RenderContext context;

    @Override
    public void init() {
        String vert = Gdx.files.internal("data/shaders/test.vertex.glsl").readString();
        String frag = Gdx.files.internal("data/shaders/test.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
    }

    @Override
    public void dispose() {
        program.dispose();
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.bind();
        program.setUniformMatrix("u_projViewTrans", camera.combined);
    }

    @Override
    public void render(Renderable renderable) {
        program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }
}
