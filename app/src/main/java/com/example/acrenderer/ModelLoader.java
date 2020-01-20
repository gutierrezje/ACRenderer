package com.example.acrenderer;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class ModelLoader {
    private static final String TAG = "ModelLoader";
    public float[] vPositions;
    public float[] vTextures;
    public float[] vNormals;
    private int faceCount;

    ModelLoader(Context context, String file) {
        Vector<Vector<Integer>> faces = new Vector<>();
        Vector<Float> vertices = new Vector<>();

        BufferedReader reader = null;
        try {
            InputStreamReader in = new InputStreamReader(context.getAssets().open(file));
            reader = new BufferedReader(in);

            String line = reader.readLine();
            String[] data;
            while (line != null) {
                // reading vertex data
                if (line.startsWith("v ")) {
                    data = line.substring(2).split(" ");
                    // position vertex
                    vertices.add(Float.parseFloat(data[0]));
                    vertices.add(Float.parseFloat(data[1]));
                    vertices.add(Float.parseFloat(data[2]));
                }
                // TODO read in other types of vertex data
                // reading face data
                else if (line.startsWith("f ")) {
                    data = line.substring(2).split(" ");

                    Vector<Integer> face = new Vector<>();
                    // parsing vertex, texture, normal indices
                    for (String indices : data) {
                        String[] vtn = indices.split("/");
                        face.add(Integer.parseInt(vtn[0]) - 1);
                        face.add(Integer.parseInt(vtn[1]) - 1);
                        face.add(Integer.parseInt(vtn[2]) - 1);
                    }
                    faces.add(face);
                }
                line = reader.readLine();
            }

        }
        catch (IOException e) {
            Log.e(TAG, "Unable to load or read file.");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    // log exception
                }
            }
        }

        faceCount = faces.size();
        vPositions = new float[faceCount * 9];
        int positionIdx = 0;
        for (Vector<Integer> face : faces) {
            for (int i = 0; i < 3; i++) {
                int index = 3 * face.get(3*i);
                vPositions[positionIdx++] = vertices.get(index++);
                vPositions[positionIdx++] = vertices.get(index++);
                vPositions[positionIdx++] = vertices.get(index);
            }
        }
    }

    public int numFaces() {
        return faceCount;
    }

//    public float getVertex(int index) {
//        return vPositions[index];
//    }
//
//    public Vector<Integer> getFace(int index) {
//        return faces.get(index);
//    }
}
