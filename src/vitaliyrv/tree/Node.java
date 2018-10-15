package vitaliyrv.tree;

public class Node {
    Node northWest;
    Node northEast;
    Node southWest;
    Node southEast;
    boolean alive;
    int level;
    double pop;

    private void initialize() {
        northWest = null;
        northEast = null;
        southWest = null;
        southEast = null;
        level = 0;
    }

    public Node(Node northWest, Node northEast, Node southWest, Node southEast) {
        this.northWest = northWest;
        this.northEast = northEast;
        this.southWest = southWest;
        this.southEast = southEast;
        level = northWest.level + 1;
        pop = northWest.pop + northEast.pop + southWest.pop + southEast.pop;
        alive = pop > 0;
    }

    public Node(boolean living) {
        initialize();
        alive = living;
        pop = alive ? 1 : 0;
    }

    public Node setBit(int x, int y) {
        if (level == 0) {
            return new Node(true);
        }
        int m_set = 1 << (level - 2);
        if (x < 0) {
            if (y < 0) {
                return new Node(northWest.setBit(x + m_set, y + m_set), northEast, southWest, southEast);
            } else {
                return new Node(northWest, northEast, southWest.setBit(x + m_set, y - m_set), southEast);
            }
        } else {
            if (y < 0) {
                return new Node(northWest, northEast.setBit(x - m_set, y + m_set), southWest, southEast);
            } else {
                return new Node(northWest, northEast, southWest, southEast.setBit(x - m_set, y - m_set));
            }
        }
    }

    public int getBit(int x, int y) {
        if (level == 0) {
            return alive ? 1 : 0;
        }
        int offset = 1 << (level - 2);
        if (x < 0) {
            if (y < 0) {
                return northWest.getBit(x + offset, y + offset);
            } else {
                return southWest.getBit(x + offset, y - offset);
            }
        } else {
            if (y < 0) {
                return northEast.getBit(x - offset, y + offset);
            } else {
                return southEast.getBit(x - offset, y - offset);
            }
        }
    }

    @Override
    public int hashCode() {
        if (level == 0) {
            return (int) pop;
        }
        return System.identityHashCode(northWest) +
                11 * System.identityHashCode(northEast) +
                101 * System.identityHashCode(southWest) +
                1007 * System.identityHashCode(southEast);
    }

    @Override
    public boolean equals(Object obj) {
        Node node = (Node) obj;
        if (obj == null || getClass() != obj.getClass() || level != node.level) {
            return false;
        }
        if (level == 0) {
            return alive == node.alive;
        }
        return northWest == node.northWest &&
                northEast == node.northEast &&
                southWest == node.southWest &&
                southEast == node.southEast;
    }

}
