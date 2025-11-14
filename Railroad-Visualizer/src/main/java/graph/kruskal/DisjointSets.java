package graph.kruskal;

/** A class that represents the Disjoint Sets data structure.
 * Please refer to the lecture slides and in-class exercises.
 * This class is used in Kruskal's. D
 * */
public class DisjointSets {
    private int[] parent;

    public void createSets(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }
    }

    /**
     * Returns the root of the "tree" that x belongs to. Uses path compression
     * heuristic.
     * @param x node id
     * @return root of the tree that x belongs to
     */
    public int find(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IllegalArgumentException();
        }
        // FILL IN CODE: go up the tree, until you reach the "root" of the tree (representative of the set)
        // referred to the lecture slides
        while(parent[x] >= 0){ // until the values go negative because then we reach a root
            x = parent[x];
        }
        return x;
    }

    /**
     * Merges the trees of x and y.
     * @param x node id
     * @param y node id
     */
    public void union(int x, int y) {
        // FILL IN CODE:
        // find the roots
        // check if both nodes are already part of one tree
        // attach a shorter tree to a taller one
        int firstRoot = find(x);
        int secRoot = find(y);
        if(secRoot == firstRoot){ return; } // roots are the same so they're in the same set, nothing to merge
        // find shorter tree out of two, then point it to the taller tree
        if(parent[firstRoot] < parent[secRoot]){ // first root is taller
            parent[secRoot] = firstRoot;
        }else if(parent[secRoot] == parent[firstRoot]){ // if their heights are the same
            parent[secRoot] = firstRoot; // make them point to the same representative
            parent[firstRoot]--;
        }else{ // the second root is larger
            parent[firstRoot] = secRoot;
        }
    }
}

