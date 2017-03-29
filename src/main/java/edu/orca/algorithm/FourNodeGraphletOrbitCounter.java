package edu.orca.algorithm;

import static edu.orca.utils.Util.adjacent;

/**
 * Created by KanthKumar on 2/22/17.
 */
public class FourNodeGraphletOrbitCounter extends OrbitCounter {

    public FourNodeGraphletOrbitCounter(OrcaGraph graph) {
        super(graph);
        this.orbit = new long[n][15];
    }

    /**
     * count graphlets on max 4 nodes
     */
    @Override
    public long[ ][ ] count() {
        // precompute triangles that span over edges
        int[ ] tri = countTriangles();

        // count full graphlets
        long[ ] C4 = new long[n];
        int[ ] neigh = new int[n];
        int nn;
        for (int x = 0; x < n; x++) {
            for (int nx = 0; nx < deg[x]; nx++) {
                int y = adj[x][nx];
                if (y >= x) break;
                nn = 0;
                for (int ny = 0; ny < deg[y]; ny++) {
                    int z = adj[y][ny];
                    if (z >= y) break;
                    if (!adjacent(adj[x], z)) continue;
                    neigh[nn++] = z;
                }
                for (int i = 0; i < nn; i++) {
                    int z = neigh[i];
                    for (int j = i + 1; j < nn; j++) {
                        int zz = neigh[j];
                        if (adjacent(adj[z], zz)) {
                            C4[x]++;
                            C4[y]++;
                            C4[z]++;
                            C4[zz]++;
                        }
                    }
                }
            }
        }

        // set up a system of equations relating orbits for every node
        int[ ] common = new int[n];
        int[ ] common_list = new int[n];
        int nc = 0;
        for (int x = 0; x < n; x++) {
            long f_12_14 = 0, f_10_13 = 0;
            long f_13_14 = 0, f_11_13 = 0;
            long f_7_11 = 0, f_5_8 = 0;
            long f_6_9 = 0, f_9_12 = 0, f_4_8 = 0, f_8_12 = 0;
            long f_14 = C4[x];

            for (int i = 0; i < nc; i++) common[common_list[i]] = 0;
            nc = 0;

            orbit[x][0] = deg[x];
            // x - middle node
            for (int nx1 = 0; nx1 < deg[x]; nx1++) {
                int y = inc[x][nx1]._1, ey = inc[x][nx1]._2;
                for (int ny = 0; ny < deg[y]; ny++) {
                    int z = inc[y][ny]._1, ez = inc[y][ny]._2;
                    if (adjacent(adj[x], z)) { // triangle
                        if (z < y) {
                            f_12_14 += tri[ez] - 1;
                            f_10_13 += (deg[y] - 1 - tri[ez]) + (deg[z] - 1 - tri[ez]);
                        }
                    } else {
                        if (common[z] == 0) common_list[nc++] = z;
                        common[z]++;
                    }
                }
                for (int nx2 = nx1 + 1; nx2 < deg[x]; nx2++) {
                    int z = inc[x][nx2]._1, ez = inc[x][nx2]._2;
                    if (adjacent(adj[y], z)) { // triangle
                        orbit[x][3]++;
                        f_13_14 += (tri[ey] - 1) + (tri[ez] - 1);
                        f_11_13 += (deg[x] - 1 - tri[ey]) + (deg[x] - 1 - tri[ez]);
                    } else { // path
                        orbit[x][2]++;
                        f_7_11 += (deg[x] - 1 - tri[ey] - 1) + (deg[x] - 1 - tri[ez] - 1);
                        f_5_8 += (deg[y] - 1 - tri[ey]) + (deg[z] - 1 - tri[ez]);
                    }
                }
            }
            // x - side node
            for (int nx1 = 0; nx1 < deg[x]; nx1++) {
                int y = inc[x][nx1]._1, ey = inc[x][nx1]._2;
                for (int ny = 0; ny < deg[y]; ny++) {
                    int z = inc[y][ny]._1, ez = inc[y][ny]._2;
                    if (x == z) continue;
                    if (!adjacent(adj[x], z)) { // path
                        orbit[x][1]++;
                        f_6_9 += (deg[y] - 1 - tri[ey] - 1);
                        f_9_12 += tri[ez];
                        f_4_8 += (deg[z] - 1 - tri[ez]);
                        f_8_12 += (common[z] - 1);
                    }
                }
            }

            // solve system of equations
            orbit[x][14] = (f_14);
            orbit[x][13] = (f_13_14 - 6 * f_14) / 2;
            orbit[x][12] = (f_12_14 - 3 * f_14);
            orbit[x][11] = (f_11_13 - f_13_14 + 6 * f_14) / 2;
            orbit[x][10] = (f_10_13 - f_13_14 + 6 * f_14);
            orbit[x][9]  = (f_9_12 - 2 * f_12_14 + 6 * f_14) / 2;
            orbit[x][8]  = (f_8_12 - 2 * f_12_14 + 6 * f_14) / 2;
            orbit[x][7]  = (f_13_14 + f_7_11 - f_11_13 - 6 * f_14) / 6;
            orbit[x][6]  = (2 * f_12_14 + f_6_9 - f_9_12 - 6 * f_14) / 2;
            orbit[x][5]  = (2 * f_12_14 + f_5_8 - f_8_12 - 6 * f_14);
            orbit[x][4]  = (2 * f_12_14 + f_4_8 - f_8_12 - 6 * f_14);
        }

        return orbit;
    }

}
