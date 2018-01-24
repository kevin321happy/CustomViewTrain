package com.wh.jxd.com.bezierview;

/**
 * Created by kevin321vip on 2018/1/22.
 * 记录一条线的起点和终点
 */

public class LineInfo {

    int lineSrartX, lineSrartY, lineEndX, lineEndY;

    public LineInfo(int lineSrartX, int lineSrartY, int lineEndX, int lineEndY) {
        this.lineSrartX = lineSrartX;
        this.lineSrartY = lineSrartY;
        this.lineEndX = lineEndX;
        this.lineEndY = lineEndY;
    }

    public LineInfo(int lineSrartX, int lineSrartY) {
        this.lineSrartX = lineSrartX;
        this.lineSrartY = lineSrartY;
    }

    public int getLineSrartX() {
        return lineSrartX;
    }

    public void setLineSrartX(int lineSrartX) {
        this.lineSrartX = lineSrartX;
    }

    public int getLineSrartY() {
        return lineSrartY;
    }

    public void setLineSrartY(int lineSrartY) {
        this.lineSrartY = lineSrartY;
    }

    public int getLineEndX() {
        return lineEndX;
    }

    public void setLineEndX(int lineEndX) {
        this.lineEndX = lineEndX;
    }

    public int getLineEndY() {
        return lineEndY;
    }

    public void setLineEndY(int lineEndY) {
        this.lineEndY = lineEndY;
    }
}
