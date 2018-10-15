package vitaliyrv.tree;

public class Tree {
    protected Node head;

    public Tree() {
        head = newNode();
    }

    public Node getHead() {
        return head;
    }

    protected Node newNode(boolean alive) {
        return new Node(alive);
    }

    protected Node newNode(Node northWest, Node northEast, Node southWest, Node southEast) {
        return new Node(northWest, northEast, southWest, southEast);
    }

    private Node newNode() {
        return emptyTree(3);
    }

    public void clear() {
        setHead(newNode());
    }

    public void setBit(int x, int y) {
        while (true) {
            int maxCoordinate = 1 << (head.level - 1);
            if (-maxCoordinate <= x && x <= maxCoordinate - 1 && -maxCoordinate <= y && y <= maxCoordinate - 1)
                break;
            expandTree();
        }
        setHead(head.setBit(x, y));
    }

    private void setHead(Node node) {
        head = node;
    }

    protected void expandTree() {
        setHead(
                newNode(
                        newNode(
                                emptyTree(head.level - 1),
                                emptyTree(head.level - 1),
                                emptyTree(head.level - 1),
                                copyTree(head.northWest)
                        ),
                        newNode(
                                emptyTree(head.level - 1),
                                emptyTree(head.level - 1),
                                copyTree(head.northEast),
                                emptyTree(head.level - 1)
                        ),
                        newNode(
                                emptyTree(head.level - 1),
                                copyTree(head.southWest),
                                emptyTree(head.level - 1),
                                emptyTree(head.level - 1)
                        ),
                        newNode(
                                copyTree(head.southEast),
                                emptyTree(head.level - 1),
                                emptyTree(head.level - 1),
                                emptyTree(head.level - 1)
                        )
                )
        );
    }


    public void randomFill(int fill_width) {
        int width = fill_width / 2;
        for (int i = -width; i < width; i++) {
            for (int q = -width; q < width; q++) {
                if (Math.random() < 0.5) {
                    setBit(i, q);
                }
            }
        }
    }

    public void squareFill(int fill_width) {
        int width = fill_width / 2;
        for (int i = -width; i < width; i++) {
            for (int q = -width; q < width; q++) {
                setBit(i, q);
            }
        }
    }

    public void nextGen() {
        while (head.level < 3 ||
                head.northWest.pop != head.northWest.southEast.southEast.pop ||
                head.northEast.pop != head.northEast.southWest.southWest.pop ||
                head.southWest.pop != head.southWest.northEast.northEast.pop ||
                head.southEast.pop != head.southEast.northWest.northWest.pop) {
            expandTree();
        }
        setHead(nextGeneration(head));
    }

    public void loadPattern(Pattern.Names n) {
        String[] pattern = Pattern.LIST.get(n.getValue()).split(",");
        int x = pattern[0].length();
        int y = pattern.length;

        for (int i = 0; i < y; i++) {
            char[] line = pattern[i].toCharArray();
            for (int j = 0; j < x; j++) {
                if (line[j] == '1') {
                    setBit(j - x / 2, i - y / 2);
                }
            }
        }
    }

    private Node copyTree(Node node) {
        if (node.level == 0) {
            return newNode(node.alive);
        }
        return newNode(
                copyTree(node.northWest),
                copyTree(node.northEast),
                copyTree(node.southWest),
                copyTree(node.southEast)
        );
    }

    private Node emptyTree(int m_level) {
        if (m_level == 0) {
            return newNode(false);
        }
        return newNode(
                emptyTree(m_level - 1),
                emptyTree(m_level - 1),
                emptyTree(m_level - 1),
                emptyTree(m_level - 1)
        );
    }

    private Node oneGen(int bitmask) {
        if (bitmask == 0) {
            return newNode(false);
        }
        int self = (bitmask >> 5) & 1;
        bitmask &= 0x757;            //1879  0111-0101-0111  вырезаем из bitmask ненужные клетки
        int neighborCount = 0;
        while (bitmask != 0) {
            neighborCount++;
            bitmask &= bitmask - 1;
        }
        if (neighborCount == 3 || (neighborCount == 2 && self != 0)) {
            return newNode(true);
        } else {
            return newNode(false);
        }
    }

    protected Node slowSimulation(Node node) {
        int allbits = 0;
        for (int y = -2; y < 2; y++) {
            for (int x = -2; x < 2; x++) {
                allbits = (allbits << 1) + node.getBit(x, y);
            }
        }
        return newNode(
                oneGen(allbits >> 5),
                oneGen(allbits >> 4),
                oneGen(allbits >> 1),
                oneGen(allbits)
        );
    }

    private Node centeredSubnode(Node node) {
        return newNode(
                copyTree(node.northWest.southEast),
                copyTree(node.northEast.southWest),
                copyTree(node.southWest.northEast),
                copyTree(node.southEast.northWest)
        );
    }

    private Node centeredHorizontal(Node west, Node east) {
        return newNode(
                copyTree(west.northEast.southEast),
                copyTree(east.northWest.southWest),
                copyTree(west.southEast.northEast),
                copyTree(east.southWest.northWest)
        );
    }

    private Node centeredVertical(Node north, Node south) {
        return newNode(
                copyTree(north.southWest.southEast),
                copyTree(north.southEast.southWest),
                copyTree(south.northWest.northEast),
                copyTree(south.northEast.northWest)
        );
    }

    private Node centeredSubSubnode(Node node) {
        return newNode(
                copyTree(node.northWest.southEast.southEast),
                copyTree(node.northEast.southWest.southWest),
                copyTree(node.southWest.northEast.northEast),
                copyTree(node.southEast.northWest.northWest)
        );
    }

    protected Node nextGeneration(Node node) {
        if (node.pop == 0) {
            return emptyTree(node.level - 1);
        }
        if (node.level == 2) {
            return slowSimulation(node);
        }
        return newNode(
                nextGeneration(
                        newNode(
                                centeredSubnode(node.northWest),
                                centeredHorizontal(node.northWest, node.northEast),
                                centeredVertical(node.northWest, node.southWest),
                                centeredSubSubnode(node)
                        )
                ),
                nextGeneration(
                        newNode(
                                centeredHorizontal(node.northWest, node.northEast),
                                centeredSubnode(node.northEast),
                                centeredSubSubnode(node),
                                centeredVertical(node.northEast, node.southEast)
                        )
                ),
                nextGeneration(
                        newNode(
                                centeredVertical(node.northWest, node.southWest),
                                centeredSubSubnode(node),
                                centeredSubnode(node.southWest),
                                centeredHorizontal(node.southWest, node.southEast)
                        )
                ),
                nextGeneration(
                        newNode(
                                centeredSubSubnode(node),
                                centeredVertical(node.northEast, node.southEast),
                                centeredHorizontal(node.southWest, node.southEast),
                                centeredSubnode(node.southEast)
                        )
                )
        );
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int level = (int) Math.pow(2, head.level);
        level = level / 2;
        for (int i = -level; i < level; i++) {
            for (int j = -level; j < level; j++) {
                result.append(head.getBit(i, j));
            }
            result.append("\n");
        }
        return result.toString();
    }

}


