public class Country{
    
    private String country;
    private boolean hasVoted;
    private String greeting;
    private String acronym;

    public Country(String name)
    {
        country = name;
        hasVoted = false;
        greeting = "";
    }
    
    public void setGreeting(String s)
    {
        greeting += s;
    }
    
    public String greeting()
    {
        return greeting;
    }
    
    public String getName()
    {
        return country;
    }
    
    public boolean hasVoted()
    {
        return hasVoted;
    }
    
    public void voted()
    {
        hasVoted = true;
    }
    
    public String toString()
    {
        return country;
    }
    
    public void setAcronym(String ac)
    {
        acronym = ac;
    }
    
    public String getAcronym()
    {
        return acronym;
    }

    public int compareTo(Country o) {
        return country.compareTo(o.getName());
    }
}
