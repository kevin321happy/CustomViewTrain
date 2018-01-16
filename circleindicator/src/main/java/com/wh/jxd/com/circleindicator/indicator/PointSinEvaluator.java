package com.wh.jxd.com.circleindicator.indicator;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Created by kevin321vip on 2018/1/16.
 */

public class PointSinEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.x + fraction * (endPoint.x - startPoint.x);

        float y = (float) (Math.sin(x * Math.PI / 180) * 100) + endPoint.y / 2;
        Point point = new Point(((int) x),(int) y);
        return point;
    }
}



