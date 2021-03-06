/*
 * Copyright 2017 Young Digital Planet S.A.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.ydp.empiria.player.client.components;

import com.google.gwt.canvas.dom.client.Context2d;
import eu.ydp.gwtutil.client.collections.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class CanvasArrow {
    private Context2d context2d;
    private final double startX, endX;
    private final double startY, endY;

    int[][] arrow = {{0, 0}, {-10, -4}, {-10, 4}};

    public CanvasArrow(Context2d context2d, double startX, double startY, double endX, double endY) {
        this.context2d = context2d;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public void draw() {
        drawLineArrow();
    }

    public void fill() {

    }

    private void drawLineArrow() {
        context2d.beginPath();
        context2d.moveTo(startX, startY);
        context2d.lineTo(endX, endY);
        context2d.stroke();
        double ang = Math.atan2(endY - startY, endX - startX);
        drawFilledPolygon(translateShape(rotateShape(arrow, ang), endX, endY));
    }

    private void drawFilledPolygon(List<KeyValue<Double, Double>> shape) {
        if (shape.size() > 0)
            context2d.beginPath();
        context2d.moveTo(shape.get(0).getKey(), shape.get(0).getValue());
        for (KeyValue<Double, Double> p : shape) {
            if (p != null)
                context2d.lineTo(p.getKey(), p.getValue());
        }
        context2d.lineTo(shape.get(0).getKey(), shape.get(0).getValue());
        context2d.fill();
        context2d.closePath();
    }

    private List<KeyValue<Double, Double>> translateShape(List<KeyValue<Double, Double>> shape, double x, double y) {
        List<KeyValue<Double, Double>> retValue = new ArrayList<KeyValue<Double, Double>>();
        for (KeyValue<Double, Double> p : shape) {
            retValue.add(new KeyValue<Double, Double>(p.getKey() + x, p.getValue() + y));
        }
        return retValue;
    }

    private List<KeyValue<Double, Double>> rotateShape(int[][] shape, double ang) {
        List<KeyValue<Double, Double>> retValue = new ArrayList<KeyValue<Double, Double>>();
        for (int[] p : shape) {
            retValue.add(rotatePoint(ang, p[0], p[1]));
        }
        return retValue;
    }

    private KeyValue<Double, Double> rotatePoint(double ang, int x, int y) {
        return new KeyValue<Double, Double>((x * Math.cos(ang)) - (y * Math.sin(ang)), (x * Math.sin(ang)) + (y * Math.cos(ang)));
    }
}
