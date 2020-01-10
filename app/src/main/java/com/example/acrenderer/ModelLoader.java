package com.example.acrenderer;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class ModelLoader {
    private Vector<Vec3> vertices;
    private Vector<Vector<Integer>> faces;
    private static final String TAG = "ModelLoader";

    ModelLoader(Context context, String file) {
        vertices = new Vector<>();
        faces = new Vector<>();

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
                    Vec3 vertex = new Vec3(
                            Float.parseFloat(data[0]),
                            Float.parseFloat(data[1]),
                            Float.parseFloat(data[2])
                    );
                    Log.d(TAG, "" + vertex);
                    vertices.add(vertex);
                }
                // reading face data
                else if (line.startsWith("f ")) {
                    data = line.substring(2).split(" ");
                    Vector<Integer> face = new Vector<>();
                    // parsing vertex, texture, normal indices
                    for (String vtn : data) {
                        face.add(Integer.parseInt(vtn.split("/")[0]) - 1);
                    }
                    Log.d(TAG, "" + face);
                    faces.add(face);
                }
                line = reader.readLine();
            }
            Log.d(TAG, "Face #: " + numFaces());
            Log.d(TAG, "Vertex #: " + numVertices());
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
    }

    public int numVertices() {
        return vertices.size();
    }

    public int numFaces() {
        return faces.size();
    }

    public Vec3 getVertex(int index) {
        return vertices.get(index);
    }

    public Vector<Integer> getFace(int index) {
        return faces.get(index);
    }
}
