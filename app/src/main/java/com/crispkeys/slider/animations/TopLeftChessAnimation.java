package com.crispkeys.slider.animations;

/**
 * Created by Behzodbek Qodirov on 8/22/15.
 */
public class TopLeftChessAnimation extends BaseChessAnimation{
    @Override
    public int[][] build(int[][] indexes, int rectCountInWidth, int rectCountInHeight) {
        int index = 0;
        for(int k = 0, max = rectCountInHeight + rectCountInWidth; k < max ; k++) {
            for(int j = 0 ; j <= k ; j++ ) {
                int i = k - j;
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
