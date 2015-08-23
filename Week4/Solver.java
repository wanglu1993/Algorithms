/****************************************************************************
 *  the programming assignment of Algorithms, Part I 
 *  Week4
 *  author: Bill Quan  
 *  Last edited: 20150823
 *  Solver.java, the second part of week4's programming assignment
 ****************************************************************************/


public class Solver 
{
    private boolean isSolvable;
    private int moves;
    private Board goalBoard;
    
    /**
     * find a solution to the initial board (using the A* algorithm)
     * @param initial the Board need to be solved
     */
    public Solver(Board initial)
    {
        MinPQ<Board> pq = new MinPQ<Board>();
        MinPQ<Board> pqTwin = new MinPQ<Board>();
        pq.insert(initial);
        pqTwin.insert(initial.twin());
        Board b;
        Board bTwin;
        while (!pq.min().isGoal() && !pqTwin.min().isGoal())
        {
            b = pq.delMin();
            bTwin = pqTwin.delMin();
            for (Board i : b.neighbors())
            {
                i.setMoved(b.getMoves() + 1);
                i.setPreviousBoard(b);
                pq.insert(i);
            }
            for (Board j : bTwin.neighbors())
            {
                j.setMoved(bTwin.getMoves() + 1);
                j.setPreviousBoard(bTwin);
                pqTwin.insert(j);
            }
        }
        if(pq.min().isGoal())
        {
            this.isSolvable = true;
            b = pq.min();
            this.moves = b.getMoves();
            goalBoard = b;
        }
        else if(pqTwin.min().isGoal())
        {
            this.isSolvable = false;
            this.moves = -1;
            goalBoard = null;
            
        }
            

        
        
    }
    

    
    
    /**
     * is the initial board solvable?
     */
    public boolean isSolvable()
    {
        return this.isSolvable;
    }
    
    /**
     * min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves()
    {
        return this.moves;
    }
    
    /**
     * sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution()
    {
        if(goalBoard == null) return null;
        Stack<Board> stack = new Stack<Board>();
        stack.push(goalBoard);
        Board b = goalBoard.previousBoard();
        while(b != null)
        {
            stack.push(b);
            b = b.previousBoard();
        }
        return stack;
    }
    
    
    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else 
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}