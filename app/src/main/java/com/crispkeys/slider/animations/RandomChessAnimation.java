package com.crispkeys.slider.animations;

/**
 * Created by Behzodbek Qodirov on 8/22/15.
 */
public class RandomChessAnimation extends BaseChessAnimation {
    private int[][] shuffle(int[][] array) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));
            int[] tmp = array[i];
            array[i] = array[r];
            array[r] = tmp;
        }
        return array;
    }

    @Override
    public int[][] build(int[][] indexes, int rectCountInWidth, int rectCountInHeight) {
        int index = 0;
        for (int x = 0; x < rectCountInWidth; x++) {
            for (int y = 0; y < rectCountInHeight; y++) {
                indexes[index][0] = x;
                indexes[index][1] = y;
                index++;
            }
        }
        return shuffle(indexes);
    }
}
