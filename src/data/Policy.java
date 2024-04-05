package data;

public enum Policy
{
    RoundRobin,
    Random;

    public static Policy stringToPolicy(String argument)
    {
        if (argument.equals("RND"))
        {
            return Policy.Random;
        }

        return Policy.RoundRobin;
    }

    public String toString()
    {
        switch(this)
        {
            case Policy.RoundRobin -> {
                return "Round Robin";
            }
            case Policy.Random -> {
                return "Random";
            }
            default -> {
                return "Something messed up";
            }
        }
    }
}
