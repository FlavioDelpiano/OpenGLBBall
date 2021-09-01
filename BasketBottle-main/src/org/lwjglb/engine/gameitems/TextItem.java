package org.lwjglb.engine.gameitems;

import org.lwjglb.engine.graph.FontTexture;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class TextItem extends GameItem {

    private static final float POSZ = 0.0f;

    private static final int VERTICES = 4;

    private final FontTexture fontTexture;

    private String text;

    public TextItem(String text, FontTexture fontTexture){
        super();
        this.text = text;
        this.fontTexture = fontTexture;
        setMesh(buildMesh());
    }

    private Mesh buildMesh() {
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals = new float[0];
        List<Integer> indices = new ArrayList<>();
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0;
        for (int i = 0; i < numChars; i++) {
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);

            // Build a character tile composed by two triangles

            // Left Top vertex
            positions.add(startx); // x
            positions.add(0.0f); //y
            positions.add(POSZ); //z
            textCoords.add((float) charInfo.getStartX() / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i * VERTICES);

            // Left Bottom vertex
            positions.add(startx); // x
            positions.add((float) fontTexture.getHeight()); //y
            positions.add(POSZ); //z
            textCoords.add((float) charInfo.getStartX() / (float) fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i * VERTICES + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add((float) fontTexture.getHeight()); //y
            positions.add(POSZ); //z
            textCoords.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i * VERTICES + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(0.0f); //y
            positions.add(POSZ); //z
            textCoords.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i * VERTICES + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i * VERTICES);
            indices.add(i * VERTICES + 2);

            startx += charInfo.getWidth();
        }

        FloatBuffer buf = FloatBuffer.allocate(positions.size());
        positions.forEach(buf::put);
        float[] posArr = buf.array();
        buf = FloatBuffer.allocate(textCoords.size());
        textCoords.forEach(buf::put);
        float[] textCoordsArr = buf.array();
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));
        return mesh;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.getMesh().deleteBuffers();
        this.setMesh(buildMesh());
    }
}