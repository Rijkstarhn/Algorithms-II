import java.util.ArrayList;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    
    private final int numberOfTeams;
    private SeparateChainingHashST<String, Team> teamTable;
    private Queue<String> certificate;
//    private int[] wins, loses, remainings;
//    private ArrayList<String> teams;
//    private int[][] games;
 
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        teamTable = new SeparateChainingHashST<String, BaseballElimination.Team>();
//        wins = new int[numberOfTeams];
//        loses = new int[numberOfTeams];
//        remainings = new int[numberOfTeams];
//        teams = new ArrayList<String>();
//        games = new int[numberOfTeams][numberOfTeams];
        for (int i = 0; i < numberOfTeams; i++) {
            Team t = new Team();
            t.vsTeams = new int[numberOfTeams];
            t.index = i; t.name = in.readString(); t.win = in.readInt(); t.lose = in.readInt(); t.remaining = in.readInt();
            teamTable.put(t.name, t);
            for (int j = 0; j < numberOfTeams; j++) {
                t.vsTeams[j] = in.readInt();
            }
//            teams.add(in.readString());
//            wins[i] = in.readInt();
//            loses[i] = in.readInt();
//            remainings[i] = in.readInt();
        }
    }
    
    private class Team {
        private int index, win, lose, remaining;
        private int[] vsTeams;
        private String name;
    }
 
    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }
 
    // all teams
    public Iterable<String> teams() {
        return teamTable.keys();
    }
 
    // number of wins for given team
    public int wins(String team) {
        if (teamTable.get(team) == null) throw new IllegalArgumentException();
        return teamTable.get(team).win;
    }
 
    // number of losses for given team
    public int losses(String team) {
        if (teamTable.get(team) == null) throw new IllegalArgumentException();
        return teamTable.get(team).lose;
    }
 
    // number of remaining games for given team
    public int remaining(String team) {
        if (teamTable.get(team) == null) throw new IllegalArgumentException();
        return teamTable.get(team).remaining;
    }
 
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (teamTable.get(team1) == null || teamTable.get(team2) == null) throw new IllegalArgumentException();
        return teamTable.get(team1).vsTeams[teamTable.get(team2).index];
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (teamTable.get(team) == null) throw new IllegalArgumentException();
        certificate = new Queue<String>();
        
        // Set up the flow net
        int V = numberOfTeams * (numberOfTeams - 1) / 2 + 2;// V = 1 + (n-1)(n-2)/2 + (n-1) + 1;
        FlowNetwork fn = new FlowNetwork(V);
        Team x = teamTable.get(team);
        teamTable.delete(team);
        ArrayList<String> teamName = new ArrayList<String>();
        for (String s : this.teams()) {
            teamName.add(s);
        }
        int L1 = 0;
        int L2 = 1 + (numberOfTeams-1)*(numberOfTeams-2)/2;
        boolean isEliminated = false;
        for (int i = 0; i < numberOfTeams-1; i++) {
            // trivial elimination
            if (x.win + x.remaining - teamTable.get(teamName.get(i)).win < 0.0) {
                isEliminated = true;
                certificate.enqueue(teamName.get(i));
                continue;
            }
            FlowEdge fe3 = new FlowEdge(L2+i, V-1, x.win + x.remaining - teamTable.get(teamName.get(i)).win);
            fn.addEdge(fe3);
        }
        if (isEliminated) {
            teamTable.put(x.name, x);
            return isEliminated;
        }
        for (int i = 0; i < numberOfTeams - 1; i++) {
            for (int j = i + 1; j < numberOfTeams - 1; j++) {
                String name1 = teamName.get(i);
                String name2 = teamName.get(j);
                FlowEdge fe = new FlowEdge(0, ++L1, against(name1, name2));
                FlowEdge fe1 = new FlowEdge(L1, L2+i, Integer.MAX_VALUE);
                FlowEdge fe2 = new FlowEdge(L1, L2+j, Integer.MAX_VALUE);
                fn.addEdge(fe); fn.addEdge(fe1); fn.addEdge(fe2);
            }
        }
        teamTable.put(x.name, x);
        
        // run the FF algorithm
        FordFulkerson ff = new FordFulkerson(fn, 0, V-1);
        
        // Judge if the team is eliminated
        for (FlowEdge fe : fn.adj(0)) {
            if (fe.flow() < fe.capacity()) {
                for (int v = 0; v < fn.V(); v++) {
                    if (ff.inCut(v) && v >= L2 && v < fn.V()-1) certificate.enqueue(teamName.get(v-L2));
                }
                return true;
            }
        }
        return false;
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (teamTable.get(team) == null) throw new IllegalArgumentException();
        if (this.isEliminated(team)) return certificate;
        else return null;
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
