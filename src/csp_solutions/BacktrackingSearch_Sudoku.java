package csp_solutions;

import core_algorithms.BacktrackingSearch;
import csp_problems.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BacktrackingSearch_Sudoku extends BacktrackingSearch<String,Integer>{

    public BacktrackingSearch_Sudoku(Sudoku problem){
        super(problem);
    }

    /**
     * To revise an arc: for each value in tail's domain, there must be a value in head's domain that's different
     *                   if not, delete the value from the tail's domain
     * @param head head of the arc to be revised
     * @param tail tail of the arc to be revised
     * @return true if the tail has been revised (lost some values), false otherwise
     */
    public boolean revise(String head, String tail)
    {
        boolean revised = false;

        List<Integer> tailDomain = getAllVariables().get(tail).domain();
        List<Integer> headDomain = getAllVariables().get(head).domain();
        List<Integer> toRemove = new ArrayList<>();
        for(Integer t : tailDomain)
        {
            boolean foundDifferentValue = false;
            for(Integer h : headDomain)
            {

                if(!t.equals(h))
                {
                    foundDifferentValue = true;
                    break;
                }
            }

            if (!foundDifferentValue) {
                toRemove.add(t);
                revised = true;
            }
        }

        for (Integer valueToRemove : toRemove) {
            tailDomain.remove(valueToRemove);
        }
        return revised;
    }

    /**
     * Implementing the minimum-remaining-values(MRV) ordering heuristic.
     * @return the variable with the smallest domain among all the unassigned variables;
     *         null if all variables have been assigned
     */
    public String selectUnassigned()
    {
        String selectedVariable = null;
        int minDomainSize = Integer.MAX_VALUE;

        for (Map.Entry<String, CSPProblem.Variable<String, Integer>> entry : getAllVariables().entrySet())
        {
            String variableName = entry.getKey();
            CSPProblem.Variable<String, Integer> variable = entry.getValue();
            
            if (variable.domain().size() > 1) {
                int domainSize = variable.domain().size();
                if (domainSize < minDomainSize) {
                    minDomainSize = domainSize;
                    selectedVariable = variableName;
                }
            }
        }

        return selectedVariable;

    }

    /**
     * @param args (no command-line argument is needed to run this program)
     */
    public static void main(String[] args) {
        String filename = "./SudokuTestCases/TestCase5.txt";
        Sudoku problem = new Sudoku(filename);
        BacktrackingSearch_Sudoku agent = new BacktrackingSearch_Sudoku(problem);
        System.out.println("loading puzzle from " + filename + "...");
        problem.printPuzzle(problem.getAllVariables());
        if(agent.initAC3() && agent.search()){
            System.out.println("Solution found:");
            problem.printPuzzle(agent.getAllVariables());
        }else{
            System.out.println("Unable to find a solution.");
        }
    }
}
