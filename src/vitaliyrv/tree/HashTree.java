package vitaliyrv.tree;

import java.util.HashMap;

public class HashTree extends Tree {

    private static HashMap hashMap = new HashMap();

    private Node nextGenNode = null;

    @Override
    protected Node newNode(boolean alive) {
        return getHashNode(new Node(alive));
    }

    @Override
    protected Node newNode(Node northWest, Node northEast, Node southWest, Node southEast) {
        return getHashNode(new Node(northWest, northEast, southWest, southEast));
    }

    private Node getHashNode(Node node) {
        Node hashNode = (Node) hashMap.get(node);
        if (hashNode != null)
            return hashNode;
        hashMap.put(node, node);
        return node;
    }

    private Node horizontalForward(Node west, Node east) {
        return nextGeneration(newNode(west.northEast, east.northWest, west.southEast, east.southWest)) ;
    }

    private  Node verticalForward(Node north, Node south) {
        return nextGeneration(newNode(north.southWest, north.southEast, south.northWest, south.northEast)) ;
    }

    private Node centerForward(Node node) {
        return nextGeneration(newNode(node.northWest.southEast, node.northEast.southWest, node.southWest.northEast, node.southEast.northWest)) ;
    }

    @Override
    protected Node nextGeneration(Node node) {
        if (nextGenNode != null)
            return nextGenNode;
        if (node.pop == 0)
            return nextGenNode = node.northWest;
        if (node.level == 2)
            return nextGenNode = slowSimulation(node);

        return nextGenNode = newNode(
                nextGeneration(
                        newNode(
                                nextGeneration(node.northWest),
                                horizontalForward(node.northWest, node.northEast),
                                verticalForward(node.northWest, node.southWest),
                                centerForward(node)
                        )
                ),
                nextGeneration(
                        newNode(
                                horizontalForward(node.northWest, node.northEast),
                                nextGeneration(node.northEast),
                                centerForward(node),
                                verticalForward(node.northEast, node.southEast)
                        )
                ),
                nextGeneration(
                        newNode(
                                verticalForward(node.northWest, node.southWest),
                                centerForward(node),
                                nextGeneration(node.southWest),
                                horizontalForward(node.southWest, node.southEast)
                        )
                ),
                nextGeneration(
                        newNode(
                                centerForward(node),
                                verticalForward(node.northEast, node.southEast),
                                horizontalForward(node.southWest, node.southEast),
                                nextGeneration(node.southEast)
                        )
                )
        );
    }

    public void nextGen(){
        while (head.level < 3 ||
                head.northWest.pop != head.northWest.southEast.southEast.pop ||
                head.northEast.pop != head.northEast.southWest.southWest.pop ||
                head.southWest.pop != head.southWest.northEast.northEast.pop ||
                head.southEast.pop != head.southEast.northWest.northWest.pop) {
            expandTree();
        }
        double stepSize = Math.pow(2.0, head.level-2);
        System.out.println("Taking a step of " + stepSize);
        head = nextGeneration(head);
    }
}
