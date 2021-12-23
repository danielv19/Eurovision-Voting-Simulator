
public class Participant extends Country implements Comparable<Participant>{

    private int votingID;
    private int votes;
    private boolean done;

    public Participant(String name, int num)
    {
        super(name);
        votingID = num;
        votes = 0;
        done = false;
    }
    
    public int getID()
    {
        return votingID;
    }

    public void done()
    {
        done = true;
    }
    
    public boolean isDone()
    {
        return done;
    }
    
    public void setPoints(int points)
    {
        votes += points;
    }
    
    public int points()
    {
        return votes;
    }
    
    @Override
    public int compareTo(Participant o) {
        if (this.points() < o.points())
        {
            return -1;
        }
        else if (this.points() > o.points())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
