package com.example.acrenderer;

class Vec4 {
    float x, y, z, w;

    Vec4() {}

    Vec4(float n) {
        x = n;
        y = n;
        z = n;
        w = 1;
    }

    Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    Vec4 add(Vec4 V) {
        return new Vec4(
                this.x + V.x,
                this.y + V.y,
                this.z + V.z,
                this.w + V.w);
    }

    Vec4 sub(Vec4 V) {
        return new Vec4(
                this.x - V.x,
                this.y - V.y,
                this.z - V.z,
                this.w - V.w);
    }

    Vec4 mul(float f) {
        return new Vec4(
                this.x * f,
                this.y * f,
                this.z * f,
                this.w * f);
    }

    float dot(Vec4 V) {
        return this.x * V.x + this.y * V.y + this.z * V.z + this.w * V.w;
    }

    void normalize() {
        this.x /= this.w;
        this.y /= this.w;
        this.z /= this.w;
        this.w /= this.w;
    }
}
