package com.example.acrenderer;

import androidx.annotation.NonNull;

public class Vec3 {

    float x, y, z;

    Vec3() {}

    Vec3(float n) {
        x = n;
        y = n;
        z = n;
    }

    Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vec3 add(Vec4 V) {
        return new Vec3(
                this.x + V.x,
                this.y + V.y,
                this.z + V.z);
    }

    Vec3 sub(Vec4 V) {
        return new Vec3(
                this.x - V.x,
                this.y - V.y,
                this.z - V.z);
    }

    Vec3 mul(float f) {
        return new Vec3(
                this.x * f,
                this.y * f,
                this.z * f);
    }

    float dot(Vec3 V) {
        return this.x * V.x + this.y * V.y + this.z * V.z;
    }

    Vec3 cross(Vec3 V) {
        return new Vec3(
                this.y * V.z - this.z * V.y,
                this.z * V.x - this.x * V.z,
                this.x * V.y - this.y * V.x
        );
    }

    void normalize(float l) {
        this.x /= l;
        this.y /= l;
        this.z /= l;
    }

    @Override
    @NonNull
    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }

}
