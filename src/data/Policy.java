package data;

/**
 * Enum representing the policy for cache replacement.
 */
public enum Policy
{
    RoundRobin,
    Random;

    /**
     * Converts a string to a Policy.
     * @param argument the string to convert
     * @return the corresponding Policy
     */
    public static Policy stringToPolicy(String argument)
    {
        if (argument.equals("RND"))
        {
            return Policy.Random;
        }

        return Policy.RoundRobin;
    }

    /**
     * Returns a string representation of the Policy.
     * @return a string representation of the Policy
     */
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
