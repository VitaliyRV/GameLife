package vitaliyrv.tree;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TreePainter {
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private double worldWidth;
    private double pointWidth = 12;


    public TreePainter(Canvas canvas) {
        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void draw(Node node) {
        clearImage();
        worldWidth = Math.pow(2, node.level);
        drawTree(node);
        drawNet();
    }

    private void clearImage() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawTree(Node node, int x, int y) {
        if (node.northWest == null) {
            fillPoint(x, y);
            return;
        }

        if (node.northWest.alive) drawTree(node.northWest, (x << 1), (y << 1));
        if (node.southWest.alive) drawTree(node.southWest, (x << 1), (y << 1) | 1);
        if (node.northEast.alive) drawTree(node.northEast, (x << 1) | 1, (y << 1));
        if (node.southEast.alive) drawTree(node.southEast, (x << 1) | 1, (y << 1) | 1);
    }

    private void drawTree(Node node) {
        drawTree(node, 0, 0);
    }

    private void fillPoint(int x, int y) {
        double width = (canvas.getWidth() - worldWidth * pointWidth) / 2;
        double height = (canvas.getHeight() - worldWidth * pointWidth) / 2;


        double x1 = pointWidth * (x) + width;
        double y1 = pointWidth * (y) + height;

        double x2 = pointWidth * (x + 1) + width + 1;
        double y2 = pointWidth * (y + 1) + height + 1;

        graphicsContext.setFill(Color.GREEN);
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.fillRect(x1, y1, x2 - x1, y2 - y1);

    }

    private void drawNet() {
        double width = (canvas.getWidth() - worldWidth * pointWidth) / 2;
        double height = (canvas.getHeight() - worldWidth * pointWidth) / 2;

        graphicsContext.setStroke(Color.SILVER);

        for (int i = 0; i <= worldWidth; i++) {
            graphicsContext.strokeLine(i * pointWidth + width, height, i * pointWidth + width, worldWidth * pointWidth + 1 + height);
        }
        for (int i = 0; i <= worldWidth; i++) {
            graphicsContext.strokeLine(
                    width,
                    i * pointWidth + height,
                    worldWidth * pointWidth + 1 + width,
                    i * pointWidth + height
            );
        }
        graphicsContext.setStroke(Color.BLACK);

        graphicsContext.strokeLine(
                worldWidth * pointWidth / 2 + width,
                height,
                worldWidth * pointWidth / 2 + width,
                worldWidth * pointWidth + height
        );
        graphicsContext.strokeLine(
                width,
                worldWidth * pointWidth / 2 + height,
                worldWidth * pointWidth + width,
                worldWidth * pointWidth / 2 + height
        );
    }

    public void setPointWidth(double pointWidth) {
        this.pointWidth = pointWidth;
    }
}
