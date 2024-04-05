package core.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Calculator
{
    public static int getBasePower(long result, int base)
    {
        return (int) Math.ceil(Math.log(result) / Math.log(base));
    }

    public static int getBasePower(int result, int base)
    {
        return getBasePower((long) result, base);
    }

    public static long getBytes(String format)
    {
        //Use this if professor Ortiz uses a KB as 1024, typically a Kibibyte (KiB) is the one that is 1024 by computing methodologies, KB is for SI units.
        int conversionFactor = 1024;
        HashMap<String, Integer> prefixes = new HashMap<>();
        prefixes.put("KB", conversionFactor);
        prefixes.put("MB", conversionFactor * prefixes.get("KB"));
        prefixes.put("GB", conversionFactor * prefixes.get("MB"));
        prefixes.put("TB", conversionFactor * prefixes.get("GB"));
        prefixes.put("PB", conversionFactor * prefixes.get("TB"));

        return calculateBytes(format, prefixes);
    }

    public static long getBytes(int digit, String prefix)
    {
        return getBytes(digit + prefix);
    }


    private static long calculateBytes(String format, HashMap<String, Integer> conversionTable)
    {
        String[] placeholder = format.split("((?<=[0-9])|(?=[0-9]))"); //Split up, but keep numbers. The prefix should be at the end
        String[] keys = conversionTable.keySet().toArray(new String[0]);

        String prefix = placeholder[placeholder.length - 1];
        long numericValue = 0L;

        //Iterate to see if it matches the correct format
        for(String s : placeholder)
        {
            if(!s.matches("[0-9]") && Arrays.stream(keys).noneMatch(str -> str.equals(s)))
            {
                System.err.printf("ERROR::Incorrect format for \"%s\"\n", format);
                return 0L;
            }

            if(s.matches("[0-9]"))
            {
                numericValue *= 10;
                numericValue += Long.parseLong(s);
            }
        }

        return numericValue * conversionTable.get(prefix);
    }

    public static String toHexString(int n)
    {
        return toHexString(n, 8);
    }

    public static String toHexString(int n, int size)
    {
        StringBuilder hex = new StringBuilder(Integer.toHexString(n));
        while(hex.length() < size) hex.insert(0, "0");
        while(hex.length() > size) hex.deleteCharAt(0);
        return hex.toString();
    }

    public static int hexToInteger(String hex)
    {
        String radix = "01234567890ABCDEF";
        hex = hex.toUpperCase();
        int integer = 0;
        for(int i = 0; i < hex.length(); i++) integer = (integer * 16) + radix.indexOf(hex.charAt(i));
        return integer;
    }

    public static int getRandomInt(int origin, int bound)
    {
        Random r = new Random();
        return r.nextInt(origin, bound);
    }

    public static int hashString(String str)
    {
        int hash = 7;
        for(int i = 0; i < str.length(); i++) hash = hash * 31 + str.charAt(i);
        return hash;
    }
}
