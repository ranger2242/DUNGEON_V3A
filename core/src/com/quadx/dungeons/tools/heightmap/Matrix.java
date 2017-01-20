package com.quadx.dungeons.tools.heightmap;

import com.quadx.dungeons.Cell;

/**
 * Created by Chris Cavazos on 1/19/2017.t
 */
public class Matrix<T> {
    private final Class<? extends T> cls;

    public Matrix(Class<? extends T> cls) {
        this.cls = cls;

    }

    public Cell[][] rotateMatrix(Cell[][] t, int res, boolean left) {
        Cell[][] n = new Cell[res][res];
        Cell[][] n2 = new Cell[res][res];

        if (!left) {
            for (int i = 0; i < res; ++i) {
                for (int j = 0; j < res; ++j) {
                    n[i][j] = t[res - j - 1][i];

                }
            }
        } else {
                for (int i = 0; i < res; ++i) {
                    for (int j = 0; j < res; ++j) {
                            n[i][j] = t[j][res - i - 1];}
                    }
                }
        return n;
    }
}

