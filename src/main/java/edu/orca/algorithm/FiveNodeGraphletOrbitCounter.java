package edu.orca.algorithm;

import javaslang.Tuple;

import java.util.HashMap;
import java.util.Map;

import static edu.orca.utils.Util.adjacent;
import static edu.orca.utils.Util.createTuple;

/**
 * Created by KanthKumar on 2/22/17.
 */
public class FiveNodeGraphletOrbitCounter extends OrbitCounter {

    private Map<Tuple, Integer> common2 = new HashMap<>();
    private Map<Tuple, Integer> common3 = new HashMap<>();

    public FiveNodeGraphletOrbitCounter(OrcaGraph graph) {
        super(graph);
        this.orbit = new long[n][73];
    }

    /**
     * count graphlets on max 5 nodes
     */
    @Override
    public long[ ][ ] count() {
        // precompute common nodes
        for (int x = 0; x < n; x++) {
            for (int n1 = 0; n1 < deg[x]; n1++) {
                int a = adj[x][n1];
                for (int n2 = n1 + 1; n2 < deg[x]; n2++) {
                    int b = adj[x][n2];
                    Tuple ab = createTuple(a, b);
                    common2.put(ab, common2.getOrDefault(ab, 0) + 1);
                    for (int n3 = n2 + 1; n3 < deg[x]; n3++) {
                        int c = adj[x][n3];
                        boolean st = adjacent(adj[a], b) ? (adjacent(adj[a], c) || adjacent(adj[b], c)) :
                                (adjacent(adj[a], c) && adjacent(adj[b], c));
                        if (!st) continue;
                        Tuple abc = createTuple(a, b, c);
                        common3.put(abc, common3.getOrDefault(abc, 0) + 1);
                    }
                }
            }
        }

        // precompute triangles that span over edges
        int[ ] tri = countTriangles();

        // count full graphlets
        long[ ] C5 = new long[n];
        int[ ] neigh = new int[n];
        int[ ] neigh2 = new int[n];
        int nn, nn2;
        for (int x = 0; x < n; x++) {
            for (int nx = 0; nx < deg[x]; nx++) {
                int y = adj[x][nx];
                if (y >= x) break;
                nn = 0;
                for (int ny = 0; ny < deg[y]; ny++) {
                    int z = adj[y][ny];
                    if (z >= y) break;
                    if (adjacent(adj[x], z)) {
                        neigh[nn++] = z;
                    }
                }
                for (int i = 0; i < nn; i++) {
                    int z = neigh[i];
                    nn2 = 0;
                    for (int j = i + 1; j < nn; j++) {
                        int zz = neigh[j];
                        if (adjacent(adj[z], zz)) {
                            neigh2[nn2++] = zz;
                        }
                    }
                    for (int i2 = 0; i2 < nn2; i2++) {
                        int zz = neigh2[i2];
                        for (int j2 = i2 + 1; j2 < nn2; j2++) {
                            int zzz = neigh2[j2];
                            if (adjacent(adj[zz], zzz)) {
                                C5[x]++;
                                C5[y]++;
                                C5[z]++;
                                C5[zz]++;
                                C5[zzz]++;
                            }
                        }
                    }
                }
            }
        }

        int[ ] common_x = new int[n];
        int[ ] common_x_list = new int[n];
        int ncx = 0;
        int[ ] common_a = new int[n];
        int[ ] common_a_list = new int[n];
        int nca = 0;

        // set up a system of equations relating orbit counts
        for (int x = 0; x < n; x++) {
            for (int i = 0; i < ncx; i++) common_x[common_x_list[i]] = 0;
            ncx = 0;

            // smaller graphlets
            orbit[x][0] = deg[x];
            for (int nx1 = 0; nx1 < deg[x]; nx1++) {
                int a = adj[x][nx1];
                for (int nx2 = nx1 + 1; nx2 < deg[x]; nx2++) {
                    int b = adj[x][nx2];
                    if (adjacent(adj[a], b)) orbit[x][3]++;
                    else orbit[x][2]++;
                }
                for (int na = 0; na < deg[a]; na++) {
                    int b = adj[a][na];
                    if (b != x && !adjacent(adj[x], b)) {
                        orbit[x][1]++;
                        if (common_x[b] == 0) common_x_list[ncx++] = b;
                        common_x[b]++;
                    }
                }
            }

            long f_71 = 0, f_70 = 0, f_67 = 0, f_66 = 0, f_58 = 0, f_57 = 0; // 14
            long f_69 = 0, f_68 = 0, f_64 = 0, f_61 = 0, f_60 = 0, f_55 = 0, f_48 = 0, f_42 = 0, f_41 = 0; // 13
            long f_65 = 0, f_63 = 0, f_59 = 0, f_54 = 0, f_47 = 0, f_46 = 0, f_40 = 0; // 12
            long f_62 = 0, f_53 = 0, f_51 = 0, f_50 = 0, f_49 = 0, f_38 = 0, f_37 = 0, f_36 = 0; // 8
            long f_44 = 0, f_33 = 0, f_30 = 0, f_26 = 0; // 11
            long f_52 = 0, f_43 = 0, f_32 = 0, f_29 = 0, f_25 = 0; // 10
            long f_56 = 0, f_45 = 0, f_39 = 0, f_31 = 0, f_28 = 0, f_24 = 0; // 9
            long f_35 = 0, f_34 = 0, f_27 = 0, f_18 = 0, f_16 = 0, f_15 = 0; // 4
            long f_17 = 0; // 5
            long f_22 = 0, f_20 = 0, f_19 = 0; // 6
            long f_23 = 0, f_21 = 0; // 7

            for (int nx1 = 0; nx1 < deg[x]; nx1++) {
                int a = inc[x][nx1]._1, xa = inc[x][nx1]._2;

                for (int i = 0; i < nca; i++) common_a[common_a_list[i]] = 0;
                nca = 0;
                for (int na = 0; na < deg[a]; na++) {
                    int b = adj[a][na];
                    for (int nb = 0; nb < deg[b]; nb++) {
                        int c = adj[b][nb];
                        if (c == a || adjacent(adj[a], c)) continue;
                        if (common_a[c] == 0) common_a_list[nca++] = c;
                        common_a[c]++;
                    }
                }

                // x = orbit-14 (tetrahedron)
                for (int nx2 = nx1 + 1; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (!adjacent(adj[a], b)) continue;
                    for (int nx3 = nx2 + 1; nx3 < deg[x]; nx3++) {
                        int c = inc[x][nx3]._1, xc = inc[x][nx3]._2;
                        if (!adjacent(adj[a], c) || !adjacent(adj[b], c)) continue;
                        orbit[x][14]++;
                        f_70 += common3.getOrDefault(createTuple(a, b, c), 0) - 1;
                        f_71 += (tri[xa] > 2 && tri[xb] > 2) ? (common3.getOrDefault(createTuple(x, a, b), 0) - 1) : 0;
                        f_71 += (tri[xa] > 2 && tri[xc] > 2) ? (common3.getOrDefault(createTuple(x, a, c), 0) - 1) : 0;
                        f_71 += (tri[xb] > 2 && tri[xc] > 2) ? (common3.getOrDefault(createTuple(x, b, c), 0) - 1) : 0;
                        f_67 += tri[xa] - 2 + tri[xb] - 2 + tri[xc] - 2;
                        f_66 += common2.getOrDefault(createTuple(a, b), 0) - 2;
                        f_66 += common2.getOrDefault(createTuple(a, c), 0) - 2;
                        f_66 += common2.getOrDefault(createTuple(b, c), 0) - 2;
                        f_58 += deg[x] - 3;
                        f_57 += deg[a] - 3 + deg[b] - 3 + deg[c] - 3;
                    }
                }

                // x = orbit-13 (diamond)
                for (int nx2 = 0; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (!adjacent(adj[a], b)) continue;
                    for (int nx3 = nx2 + 1; nx3 < deg[x]; nx3++) {
                        int c = inc[x][nx3]._1, xc = inc[x][nx3]._2;
                        if (!adjacent(adj[a], c) || adjacent(adj[b], c)) continue;
                        orbit[x][13]++;
                        f_69 += (tri[xb] > 1 && tri[xc] > 1) ? (common3.getOrDefault(createTuple(x, b, c), 0) - 1) : 0;
                        f_68 += common3.getOrDefault(createTuple(a, b, c), 0) - 1;
                        f_64 += common2.getOrDefault(createTuple(b, c), 0) - 2;
                        f_61 += tri[xb] - 1 + tri[xc] - 1;
                        f_60 += common2.getOrDefault(createTuple(a, b), 0) - 1;
                        f_60 += common2.getOrDefault(createTuple(a, c), 0) - 1;
                        f_55 += tri[xa] - 2;
                        f_48 += deg[b] - 2 + deg[c] - 2;
                        f_42 += deg[x] - 3;
                        f_41 += deg[a] - 3;
                    }
                }

                // x = orbit-12 (diamond)
                for (int nx2 = nx1 + 1; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (!adjacent(adj[a], b)) continue;
                    for (int na = 0; na < deg[a]; na++) {
                        int c = inc[a][na]._1, ac = inc[a][na]._2;
                        if (c == x || adjacent(adj[x], c) || !adjacent(adj[b], c)) continue;
                        orbit[x][12]++;
                        f_65 += (tri[ac] > 1) ? common3.getOrDefault(createTuple(a, b, c), 0) : 0;
                        f_63 += common_x[c] - 2;
                        f_59 += tri[ac] - 1 + common2.getOrDefault(createTuple(b, c), 0) - 1;
                        f_54 += common2.getOrDefault(createTuple(a, b), 0) - 2;
                        f_47 += deg[x] - 2;
                        f_46 += deg[c] - 2;
                        f_40 += deg[a] - 3 + deg[b] - 3;
                    }
                }

                // x = orbit-8 (cycle)
                for (int nx2 = nx1 + 1; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (adjacent(adj[a], b)) continue;
                    for (int na = 0; na < deg[a]; na++) {
                        int c = inc[a][na]._1, ac = inc[a][na]._2;
                        if (c == x || adjacent(adj[x], c) || !adjacent(adj[b], c)) continue;
                        orbit[x][8]++;
                        f_62 += (tri[ac] > 0) ? common3.getOrDefault(createTuple(a, b, c), 0) : 0;
                        f_53 += tri[xa] + tri[xb];
                        f_51 += tri[ac] + common2.getOrDefault(createTuple(c, b), 0);
                        f_50 += common_x[c] - 2;
                        f_49 += common_a[b] - 2;
                        f_38 += deg[x] - 2;
                        f_37 += deg[a] - 2 + deg[b] - 2;
                        f_36 += deg[c] - 2;
                    }
                }

                // x = orbit-11 (paw)
                for (int nx2 = nx1 + 1; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (!adjacent(adj[a], b)) continue;
                    for (int nx3 = 0; nx3 < deg[x]; nx3++) {
                        int c = inc[x][nx3]._1, xc = inc[x][nx3]._2;
                        if (c == a || c == b || adjacent(adj[a], c) || adjacent(adj[b], c)) continue;
                        orbit[x][11]++;
                        f_44 += tri[xc];
                        f_33 += deg[x] - 3;
                        f_30 += deg[c] - 1;
                        f_26 += deg[a] - 2 + deg[b] - 2;
                    }
                }

                // x = orbit-10 (paw)
                for (int nx2 = 0; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (!adjacent(adj[a], b)) continue;
                    for (int nb = 0; nb < deg[b]; nb++) {
                        int c = inc[b][nb]._1, bc = inc[b][nb]._2;
                        if (c == x || c == a || adjacent(adj[a], c) || adjacent(adj[x], c)) continue;
                        orbit[x][10]++;
                        f_52 += common_a[c] - 1;
                        f_43 += tri[bc];
                        f_32 += deg[b] - 3;
                        f_29 += deg[c] - 1;
                        f_25 += deg[a] - 2;
                    }
                }

                // x = orbit-9 (paw)
                for (int na1 = 0; na1 < deg[a]; na1++) {
                    int b = inc[a][na1]._1, ab = inc[a][na1]._2;
                    if (b == x || adjacent(adj[x], b)) continue;
                    for (int na2 = na1 + 1; na2 < deg[a]; na2++) {
                        int c = inc[a][na2]._1, ac = inc[a][na2]._2;
                        if (c == x || !adjacent(adj[b], c) || adjacent(adj[x], c)) continue;
                        orbit[x][9]++;
                        f_56 += (tri[ab] > 1 && tri[ac] > 1) ? common3.getOrDefault(createTuple(a, b, c), 0) : 0;
                        f_45 += common2.getOrDefault(createTuple(b, c), 0) - 1;
                        f_39 += tri[ab] - 1 + tri[ac] - 1;
                        f_31 += deg[a] - 3;
                        f_28 += deg[x] - 1;
                        f_24 += deg[b] - 2 + deg[c] - 2;
                    }
                }

                // x = orbit-4 (path)
                for (int na = 0; na < deg[a]; na++) {
                    int b = inc[a][na]._1, ab = inc[a][na]._2;
                    if (b == x || adjacent(adj[x], b)) continue;
                    for (int nb = 0; nb < deg[b]; nb++) {
                        int c = inc[b][nb]._1, bc = inc[b][nb]._2;
                        if (c == a || adjacent(adj[a], c) || adjacent(adj[x], c)) continue;
                        orbit[x][4]++;
                        f_35 += common_a[c] - 1;
                        f_34 += common_x[c];
                        f_27 += tri[bc];
                        f_18 += deg[b] - 2;
                        f_16 += deg[x] - 1;
                        f_15 += deg[c] - 1;
                    }
                }

                // x = orbit-5 (path)
                for (int nx2 = 0; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (b == a || adjacent(adj[a], b)) continue;
                    for (int nb = 0; nb < deg[b]; nb++) {
                        int c = inc[b][nb]._1, bc = inc[b][nb]._2;
                        if (c == x || adjacent(adj[a], c) || adjacent(adj[x], c)) continue;
                        orbit[x][5]++;
                        f_17 += deg[a] - 1;
                    }
                }

                // x = orbit-6 (claw)
                for (int na1 = 0; na1 < deg[a]; na1++) {
                    int b = inc[a][na1]._1, ab = inc[a][na1]._2;
                    if (b == x || adjacent(adj[x], b)) continue;
                    for (int na2 = na1 + 1; na2 < deg[a]; na2++) {
                        int c = inc[a][na2]._1, ac = inc[a][na2]._2;
                        if (c == x || adjacent(adj[x], c) || adjacent(adj[b], c)) continue;
                        orbit[x][6]++;
                        f_22 += deg[a] - 3;
                        f_20 += deg[x] - 1;
                        f_19 += deg[b] - 1 + deg[c] - 1;
                    }
                }

                // x = orbit-7 (claw)
                for (int nx2 = nx1 + 1; nx2 < deg[x]; nx2++) {
                    int b = inc[x][nx2]._1, xb = inc[x][nx2]._2;
                    if (adjacent(adj[a], b)) continue;
                    for (int nx3 = nx2 + 1; nx3 < deg[x]; nx3++) {
                        int c = inc[x][nx3]._1, xc = inc[x][nx3]._2;
                        if (adjacent(adj[a], c) || adjacent(adj[b], c)) continue;
                        orbit[x][7]++;
                        f_23 += deg[x] - 3;
                        f_21 += deg[a] - 1 + deg[b] - 1 + deg[c] - 1;
                    }
                }
            }

            // solve equations
            orbit[x][72] = C5[x];
            orbit[x][71] = (f_71 - 12 * orbit[x][72]) / 2;
            orbit[x][70] = (f_70 - 4 * orbit[x][72]);
            orbit[x][69] = (f_69 - 2 * orbit[x][71]) / 4;
            orbit[x][68] = (f_68 - 2 * orbit[x][71]);
            orbit[x][67] = (f_67 - 12 * orbit[x][72] - 4 * orbit[x][71]);
            orbit[x][66] = (f_66 - 12 * orbit[x][72] - 2 * orbit[x][71] - 3 * orbit[x][70]);
            orbit[x][65] = (f_65 - 3 * orbit[x][70]) / 2;
            orbit[x][64] = (f_64 - 2 * orbit[x][71] - 4 * orbit[x][69] - 1 * orbit[x][68]);
            orbit[x][63] = (f_63 - 3 * orbit[x][70] - 2 * orbit[x][68]);
            orbit[x][62] = (f_62 - 1 * orbit[x][68]) / 2;
            orbit[x][61] = (f_61 - 4 * orbit[x][71] - 8 * orbit[x][69] - 2 * orbit[x][67]) / 2;
            orbit[x][60] = (f_60 - 4 * orbit[x][71] - 2 * orbit[x][68] - 2 * orbit[x][67]);
            orbit[x][59] = (f_59 - 6 * orbit[x][70] - 2 * orbit[x][68] - 4 * orbit[x][65]);
            orbit[x][58] = (f_58 - 4 * orbit[x][72] - 2 * orbit[x][71] - 1 * orbit[x][67]);
            orbit[x][57] = (f_57 - 12 * orbit[x][72] - 4 * orbit[x][71] - 3 * orbit[x][70] - 1 * orbit[x][67] - 2 * orbit[x][66]);
            orbit[x][56] = (f_56 - 2 * orbit[x][65]) / 3;
            orbit[x][55] = (f_55 - 2 * orbit[x][71] - 2 * orbit[x][67]) / 3;
            orbit[x][54] = (f_54 - 3 * orbit[x][70] - 1 * orbit[x][66] - 2 * orbit[x][65]) / 2;
            orbit[x][53] = (f_53 - 2 * orbit[x][68] - 2 * orbit[x][64] - 2 * orbit[x][63]);
            orbit[x][52] = (f_52 - 2 * orbit[x][66] - 2 * orbit[x][64] - 1 * orbit[x][59]) / 2;
            orbit[x][51] = (f_51 - 2 * orbit[x][68] - 2 * orbit[x][63] - 4 * orbit[x][62]);
            orbit[x][50] = (f_50 - 1 * orbit[x][68] - 2 * orbit[x][63]) / 3;
            orbit[x][49] = (f_49 - 1 * orbit[x][68] - 1 * orbit[x][64] - 2 * orbit[x][62]) / 2;
            orbit[x][48] = (f_48 - 4 * orbit[x][71] - 8 * orbit[x][69] - 2 * orbit[x][68] - 2 * orbit[x][67] - 2 * orbit[x][64] - 2 * orbit[x][61] - 1 * orbit[x][60]);
            orbit[x][47] = (f_47 - 3 * orbit[x][70] - 2 * orbit[x][68] - 1 * orbit[x][66] - 1 * orbit[x][63] - 1 * orbit[x][60]);
            orbit[x][46] = (f_46 - 3 * orbit[x][70] - 2 * orbit[x][68] - 2 * orbit[x][65] - 1 * orbit[x][63] - 1 * orbit[x][59]);
            orbit[x][45] = (f_45 - 2 * orbit[x][65] - 2 * orbit[x][62] - 3 * orbit[x][56]);
            orbit[x][44] = (f_44 - 1 * orbit[x][67] - 2 * orbit[x][61]) / 4;
            orbit[x][43] = (f_43 - 2 * orbit[x][66] - 1 * orbit[x][60] - 1 * orbit[x][59]) / 2;
            orbit[x][42] = (f_42 - 2 * orbit[x][71] - 4 * orbit[x][69] - 2 * orbit[x][67] - 2 * orbit[x][61] - 3 * orbit[x][55]);
            orbit[x][41] = (f_41 - 2 * orbit[x][71] - 1 * orbit[x][68] - 2 * orbit[x][67] - 1 * orbit[x][60] - 3 * orbit[x][55]);
            orbit[x][40] = (f_40 - 6 * orbit[x][70] - 2 * orbit[x][68] - 2 * orbit[x][66] - 4 * orbit[x][65] - 1 * orbit[x][60] - 1 * orbit[x][59] - 4 * orbit[x][54]);
            orbit[x][39] = (f_39 - 4 * orbit[x][65] - 1 * orbit[x][59] - 6 * orbit[x][56]) / 2;
            orbit[x][38] = (f_38 - 1 * orbit[x][68] - 1 * orbit[x][64] - 2 * orbit[x][63] - 1 * orbit[x][53] - 3 * orbit[x][50]);
            orbit[x][37] = (f_37 - 2 * orbit[x][68] - 2 * orbit[x][64] - 2 * orbit[x][63] - 4 * orbit[x][62] - 1 * orbit[x][53] - 1 * orbit[x][51] - 4 * orbit[x][49]);
            orbit[x][36] = (f_36 - 1 * orbit[x][68] - 2 * orbit[x][63] - 2 * orbit[x][62] - 1 * orbit[x][51] - 3 * orbit[x][50]);
            orbit[x][35] = (f_35 - 1 * orbit[x][59] - 2 * orbit[x][52] - 2 * orbit[x][45]) / 2;
            orbit[x][34] = (f_34 - 1 * orbit[x][59] - 2 * orbit[x][52] - 1 * orbit[x][51]) / 2;
            orbit[x][33] = (f_33 - 1 * orbit[x][67] - 2 * orbit[x][61] - 3 * orbit[x][58] - 4 * orbit[x][44] - 2 * orbit[x][42]) / 2;
            orbit[x][32] = (f_32 - 2 * orbit[x][66] - 1 * orbit[x][60] - 1 * orbit[x][59] - 2 * orbit[x][57] - 2 * orbit[x][43] - 2 * orbit[x][41] - 1 * orbit[x][40]) / 2;
            orbit[x][31] = (f_31 - 2 * orbit[x][65] - 1 * orbit[x][59] - 3 * orbit[x][56] - 1 * orbit[x][43] - 2 * orbit[x][39]);
            orbit[x][30] = (f_30 - 1 * orbit[x][67] - 1 * orbit[x][63] - 2 * orbit[x][61] - 1 * orbit[x][53] - 4 * orbit[x][44]);
            orbit[x][29] = (f_29 - 2 * orbit[x][66] - 2 * orbit[x][64] - 1 * orbit[x][60] - 1 * orbit[x][59] - 1 * orbit[x][53] - 2 * orbit[x][52] - 2 * orbit[x][43]);
            orbit[x][28] = (f_28 - 2 * orbit[x][65] - 2 * orbit[x][62] - 1 * orbit[x][59] - 1 * orbit[x][51] - 1 * orbit[x][43]);
            orbit[x][27] = (f_27 - 1 * orbit[x][59] - 1 * orbit[x][51] - 2 * orbit[x][45]) / 2;
            orbit[x][26] = (f_26 - 2 * orbit[x][67] - 2 * orbit[x][63] - 2 * orbit[x][61] - 6 * orbit[x][58] - 1 * orbit[x][53] - 2 * orbit[x][47] - 2 * orbit[x][42]);
            orbit[x][25] = (f_25 - 2 * orbit[x][66] - 2 * orbit[x][64] - 1 * orbit[x][59] - 2 * orbit[x][57] - 2 * orbit[x][52] - 1 * orbit[x][48] - 1 * orbit[x][40]) / 2;
            orbit[x][24] = (f_24 - 4 * orbit[x][65] - 4 * orbit[x][62] - 1 * orbit[x][59] - 6 * orbit[x][56] - 1 * orbit[x][51] - 2 * orbit[x][45] - 2 * orbit[x][39]);
            orbit[x][23] = (f_23 - 1 * orbit[x][55] - 1 * orbit[x][42] - 2 * orbit[x][33]) / 4;
            orbit[x][22] = (f_22 - 2 * orbit[x][54] - 1 * orbit[x][40] - 1 * orbit[x][39] - 1 * orbit[x][32] - 2 * orbit[x][31]) / 3;
            orbit[x][21] = (f_21 - 3 * orbit[x][55] - 3 * orbit[x][50] - 2 * orbit[x][42] - 2 * orbit[x][38] - 2 * orbit[x][33]);
            orbit[x][20] = (f_20 - 2 * orbit[x][54] - 2 * orbit[x][49] - 1 * orbit[x][40] - 1 * orbit[x][37] - 1 * orbit[x][32]);
            orbit[x][19] = (f_19 - 4 * orbit[x][54] - 4 * orbit[x][49] - 1 * orbit[x][40] - 2 * orbit[x][39] - 1 * orbit[x][37] - 2 * orbit[x][35] - 2 * orbit[x][31]);
            orbit[x][18] = (f_18 - 1 * orbit[x][59] - 1 * orbit[x][51] - 2 * orbit[x][46] - 2 * orbit[x][45] - 2 * orbit[x][36] - 2 * orbit[x][27] - 1 * orbit[x][24]) / 2;
            orbit[x][17] = (f_17 - 1 * orbit[x][60] - 1 * orbit[x][53] - 1 * orbit[x][51] - 1 * orbit[x][48] - 1 * orbit[x][37] - 2 * orbit[x][34] - 2 * orbit[x][30]) / 2;
            orbit[x][16] = (f_16 - 1 * orbit[x][59] - 2 * orbit[x][52] - 1 * orbit[x][51] - 2 * orbit[x][46] - 2 * orbit[x][36] - 2 * orbit[x][34] - 1 * orbit[x][29]);
            orbit[x][15] = (f_15 - 1 * orbit[x][59] - 2 * orbit[x][52] - 1 * orbit[x][51] - 2 * orbit[x][45] - 2 * orbit[x][35] - 2 * orbit[x][34] - 2 * orbit[x][27]);
        }

        return orbit;
    }

}
