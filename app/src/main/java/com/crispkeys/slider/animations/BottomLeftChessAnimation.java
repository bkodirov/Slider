package com.crispkeys.slider.animations;

/**
 * Created by Behzodbek Qodirov on 8/22/15.
 */
public class BottomLeftChessAnimation extends BaseChessAnimation {

    @Override
    public int[][] build(int[][] indexes, int rectCountInWidth, int rectCountInHeight) {
        int index = 0;
        for (int k = 0; k < rectCountInHeight + rectCountInWidth; ++k) {
            int z = k < rectCountInHeight ? 0 : k - rectCountInHeight + 1;
            for (int i = z; i <= k - z; ++i) {
                int j = (rectCountInHeight -1)-(k-i);
                if(i < rectCountInWidth && j < rectCountInHeight) {
                    indexes[index][0] = i;
                    indexes[index][1] = j;
                    index++;
                }
            }
        }
        return indexes;
    }
}
