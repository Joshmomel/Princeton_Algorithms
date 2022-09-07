import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BaseballElimination {
  // create a baseball division from given filename in format specified below

  private int numberOfTeams;

  private final int teamCombinations;
  private final int vertices;
  private final ArrayList<String> teams = new ArrayList<>();
  private final HashMap<String, Integer> wins = new HashMap<>();
  private final HashMap<String, Integer> losses = new HashMap<>();
  private final HashMap<String, Integer> remainGames = new HashMap<>();
  private final HashMap<String, int[]> remainGamesAgainstTeams = new HashMap<>();


  public BaseballElimination(String filename) {
    readFileWithInit(filename);

    int otherTeams = this.numberOfTeams - 1;
    int competeTeams = otherTeams - 1;
    this.teamCombinations = ((1 + competeTeams) * competeTeams / 2);
    this.vertices = 2 + otherTeams + this.teamCombinations;

  }

  private void readFileWithInit(String filename) {
    In in = new In(filename);
    String teamNumber = in.readLine();
    this.numberOfTeams = Integer.parseInt(teamNumber);
    while (!in.isEmpty()) {
      String s = in.readLine().trim();
      String[] teamData = s.split("\\s+");
      teams.add(teamData[0]);
      wins.put(teamData[0], Integer.parseInt(teamData[1]));
      losses.put(teamData[0], Integer.parseInt(teamData[2]));
      remainGames.put(teamData[0], Integer.parseInt(teamData[3]));

      int[] gamesAgainstTeams = new int[this.numberOfTeams];
      for (int i = 0; i < this.numberOfTeams; i++) {
        gamesAgainstTeams[i] = Integer.parseInt(teamData[4 + i]);
      }

      remainGamesAgainstTeams.put(teamData[0], gamesAgainstTeams);
    }
  }

  // number of teams
  public int numberOfTeams() {
    return this.numberOfTeams;
  }

  // all teams
  public Iterable<String> teams() {
    return this.teams;
  }

  // number of wins for given team
  public int wins(String team) {
    if (!teams.contains(team)) {
      throw new IllegalArgumentException();
    }
    return this.wins.get(team);
  }

  // number of losses for given team
  public int losses(String team) {
    if (!teams.contains(team)) {
      throw new IllegalArgumentException();
    }
    return this.losses.get(team);
  }

  // number of remaining games for given team
  public int remaining(String team) {
    if (!teams.contains(team)) {
      throw new IllegalArgumentException();
    }

    return this.remainGames.get(team);
  }

  // number of remaining games between team1 and team2
  public int against(String team1, String team2) {
    if (!teams.contains(team1) || !teams.contains(team2)) {
      throw new IllegalArgumentException();
    }
    int i = teams.indexOf(team2);
    int[] ints = this.remainGamesAgainstTeams.get(team1);
    return ints[i];
  }

  // is given team eliminated?
  public boolean isEliminated(String team) {
    if (!teams.contains(team)) {
      throw new IllegalArgumentException();
    }
    return this.certificateOfElimination(team) != null;
  }

  // subset R of teams that eliminates given team; null if not eliminated
  public Iterable<String> certificateOfElimination(String team) {
    if (!teams.contains(team)) {
      throw new IllegalArgumentException();
    }

    FlowNetwork flowNetwork = new FlowNetwork(this.vertices);
    List<String> computeTeams = teams.stream().filter(s -> !s.equals(team)).collect(Collectors.toList());

    int computeTeamLength = computeTeams.size();
    int counter = 0;
    for (int i = 0; i < computeTeamLength; i++) {
      for (int j = i + 1; j < computeTeamLength; j++) {
        counter += 1;
        flowNetwork.addEdge(new FlowEdge(0, counter, this.against(computeTeams.get(i), computeTeams.get(j))));
        flowNetwork.addEdge(new FlowEdge(counter, i + 1 + this.teamCombinations, Double.POSITIVE_INFINITY));
        flowNetwork.addEdge(new FlowEdge(counter, j + 1 + this.teamCombinations, Double.POSITIVE_INFINITY));
      }
    }

    ArrayList<String> teamList = new ArrayList<>();
    int compareTeam = this.wins(teams.get(teams.indexOf(team))) + this.remaining(teams.get(teams.indexOf(team)));
    int i = 0;
    for (String computeTeam : computeTeams) {
      i += 1;
      if (compareTeam - this.wins(computeTeam) < 0) {
        teamList.add(computeTeam);
        return teamList;
      }
      flowNetwork.addEdge(new FlowEdge(i + this.teamCombinations, this.vertices - 1, compareTeam - this.wins(computeTeam)));
    }

    FordFulkerson maxflow = new FordFulkerson(flowNetwork, 0, this.vertices - 1);
    for (int v = 0; v < flowNetwork.V(); v++) {
      if (maxflow.inCut(v)) {
        int teamIndex = v - this.teamCombinations - 1;
        if (teamIndex >= 0) {
          String name = computeTeams.get(teamIndex);
          teamList.add(name);
        }
      }
    }

    return teamList.size() > 0 ? teamList : null;
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
      } else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }
}
