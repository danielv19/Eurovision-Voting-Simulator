import java.io.*;
import java.util.*;

public class SimulationRunner {
    private static Country[] voters;
    private static Participant[] finalists;
    private static Country[] all;

    
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Random rand = new Random();
        Long seed = rand.nextLong();
        rand.setSeed(seed);
        System.out.print("========================================================================\n" + 
                "\t\tWelcome to the Eurovision Simulator\n" + 
                "========================================================================\n" + 
                "Please type the number of participants (including non qualifiers):");

        int total = Integer.valueOf(input.nextLine());
        while (total < 11)
        {
            System.out.print("Please type a value greater than 10:");
            total = Integer.valueOf(input.nextLine());
        }
        System.out.print("Please type the number of finalists:");
        int finale = Integer.valueOf(input.nextLine());
        while (total < finale || finale < 11)
        {
            if (total < finale)
            {
                System.out.print("Number of finalists can't be greater than total participants:");
                finale = Integer.valueOf(input.nextLine());
            }
            else if (finale < 11)
            {
                System.out.print("There must be 11 finalist minimum:");
                finale = Integer.valueOf(input.nextLine());
            }
        }
        voters = new Country[total];
        all = new Country[75];
        finalists = new Participant[finale];
        BST valid = setupAcronym(all);
        System.out.print("========================================================================\n"
                + "Would you like to randomize the "+ finale + " finalists? (y/n): ");
        String yn = input.nextLine().toUpperCase();
        boolean yessir = false;
        if (yn.equals("Y") || yn.equals("YES"))
        {
            yessir = true;
        }
        chooseF(finalists, all, input, valid, yessir, rand);

        System.out.println("Here is the list of all countries in the final:");
        for (int i = 0; i < finalists.length; i++)
        {
            System.out.println("#" + (i+1) + " " + finalists[i].getName() + " (" + finalists[i].getAcronym() + ")");
        }
        for (int i = 0; i < finalists.length; i++)
        {
            voters[i] = finalists[i];
        }
        if (finale != total)
        {
            System.out.print("========================================================================\n"
                    + "Would you like to randomize the "+ (total - finale) + " participants (non participants)? (y/n): ");
            yn = input.nextLine().toUpperCase();

            yessir = false;
            if (yn.equals("Y") || yn.equals("YES"))
            {
                yessir = true;
            }
            chooseP(voters, finalists, all, input, valid, yessir, rand);
            System.out.println("Here is the list of the remaining voting countries:");
            for (int i = finalists.length; i < voters.length; i++)
            {
                System.out.println("#" + (i+1) + " " + voters[i].getName());
            }
        }
        for (int i = 0; i < voters.length; i++)
        {
            BST recieved = loadFinalists(finalists, voters[i]);
            System.out.print("========================================================================\n"
                    + voters[i].greeting() +", " + line(voters[i]) + "\t(" + (i+1) + "/" + voters.length + ")\n");
            System.out.print("Would you like to randomize this country's voting ? (y/n): ");
            yn = input.nextLine().toUpperCase();
            yessir = false;
            if (yn.equals("Y") || yn.equals("YES"))
            {
                yessir = true;
            }
            vote(finalists, recieved, input, yessir, rand);
            if (i < voters.length-1)
            {
                top10(finalists, false);
            }
        }
        finalPrint(finalists, false);
        System.out.print("========================================================================\n");
        System.out.print("Would you like to randomize the televote ? (y/n): ");
        yn = input.nextLine().toUpperCase();
        yessir = false;
        if (yn.equals("Y") || yn.equals("YES"))
        {
            yessir = true;
        }
        televote(finalists, voters, input, yessir, rand);
        finalPrint(finalists, true);
}
    private static void finalPrint(Participant[] f, boolean isFinal)
    {
        int loop = (f.length/2) + (f.length%2);
        Participant[] finalSB = new Participant[f.length];
        MaxHeap heap = new MaxHeap(f.length);
        for (int i = 0; i < f.length; i++)
        {
            heap.insert(f[i]);
        }
        for (int i = 0; i < f.length; i++)
        {
            finalSB[i] = heap.removemax();
        }
        if (isFinal)
        {
            System.out.println("========================================================================\n" + 
                    "\t\t\tFINAL SCOREBOARD");
        }
        else
        {
            System.out.println("========================================================================\n" + 
                    "\t\t\t      SCOREBOARD");
        }
        for (int i = 0; i < loop; i++)
        {
            String s = "[" + (i+1) + "] " + finalSB[i].getName() + ": " + finalSB[i].points();
            System.out.printf("\t%-32s", s);
            if (f.length % 2 == 0 || i + 1 < loop)
            {
                System.out.println("\t[" + (i+1+loop) + "] " + finalSB[i+loop].getName() + ": " + finalSB[i+loop].points());
            }
            else if ( i + 1 == loop)
            {
                System.out.print("\n");
            }
        }
        if(isFinal)
        {
            System.out.println("========================================================================");
            System.out.println("\tCONGRATS TO " + finalSB[0].getName() + " ON WINNING EUROVISION WITH " + finalSB[0].points() + " POINTS!");
            System.out.println("========================================================================");
        }
    }
        
    private static void televote(Participant[] f, Country[] voters, Scanner in, boolean is, Random rand)
    {
        int[] televote = new int[f.length];
        for (int i = 0; i < voters.length; i++)
        {
            BST recieved = loadFinalists(f, voters[i]);
            for (int j = 0; j < 12; j++)
            {
                if (j + 1 > 8)
                {
                    j++;
                }
                int id = rand.nextInt(f.length);
                Country country = f[id];
                BSTNode acronym = recieved.find(country);
                while (acronym == null)
                {
                    id = rand.nextInt(f.length);
                    country = f[id];
                    acronym = recieved.find(country);
                }
                televote[id] += j + 1;
                recieved.remove(country);
            }
        }

        Arrays.sort(televote);

        if (is)
        {
            MinHeap heap = new MinHeap(f.length);
            for (int i = 0; i < f.length; i++)
            {
                heap.insert(f[i]);
            }
            
            for (int i = 0; i < televote.length; i++)
            {
                int id = rand.nextInt(televote.length);
                while (televote[id] < 0)
                {
                    id = rand.nextInt(televote.length);
                }
                Participant receiver = heap.removemin();
                receiver.setPoints(televote[id]);
                top10(f, true);
                System.out.println(receiver.getName() + " has gotten " + televote[id] + " points! (" + (i+1) + "/" + televote.length + ")");
                televote[id] = -1;
                String re = in.nextLine();
            }
        }
        else
        {
            BST recieved = loadFinalists(f, new Country(""));
            for (int i = 0; i < televote.length; i++)
            {
                System.out.print("(" + (i+1) + "/" + televote.length + ") " + televote[i] + " points go to: ");
                String country = in.nextLine().toUpperCase();
                Country finalist = new Country(country);
                BSTNode pais = recieved.find(finalist);
                while (pais == null)
                {
                    System.out.println(country + " is not applicable please enter another country");
                    System.out.print((i+1) + " point: ");
                    country = in.nextLine().toUpperCase();
                    finalist = new Country(country);
                    pais = recieved.find(finalist);
                }
                Participant p = (Participant) pais.value();
                f[p.getID()].setPoints(i+1);
                recieved.remove(pais.value());
                top10(f, true);
            }
        }
    }
    
    private static void top10(Participant[] f, boolean top5)
    {
        MaxHeap heap = new MaxHeap(f.length);
        for (int i = 0; i < f.length; i++)
        {
            heap.insert(f[i]);
        }
        if (top5)
        {
            System.out.println("\t\t\tTop 5 Currently");
            for (int i = 0; i < 5; i++)
            {
                Participant finalist = heap.removemax();
                System.out.println("[" + (i+1) + "] " + finalist.getName() + ": " + finalist.points());
            }
            System.out.println();
        }
        else
        {
            System.out.println("========================================================================\n" + 
                    "\t\t\tTop 10 Currently");
            for (int i = 0; i < 10; i++)
            {
                Participant finalist = heap.removemax();
                System.out.println("[" + (i+1) + "] " + finalist.getName() + ": " + finalist.points());
            }
        }
    }
    
    private static void vote(Participant[] f, BST finalists, Scanner in, boolean is, Random rand)
    {
        if (is)
        {
            for (int i = 0; i < 12; i++)
            {
                if (i + 1 > 8)
                {
                    i++;
                }
                int id = rand.nextInt(f.length);
                Country country = f[id];
                BSTNode acronym = finalists.find(country);
                while (acronym == null)
                {
                    id = rand.nextInt(f.length);
                    country = f[id];
                    acronym = finalists.find(country);
                }
                System.out.println((i+1) + " point: " + country.getName());
                f[id].setPoints(i+1);
                finalists.remove(country);
            }
        }
        else
        {
            for (int i = 0; i < 12; i++)
            {
                if (i + 1 > 8)
                {
                    i++;
                }
                System.out.print((i+1) + " point: ");
                String country = in.nextLine().toUpperCase();
                Country finalist = new Country(country);
                BSTNode pais = finalists.find(finalist);
                while (pais == null)
                {
                    System.out.println(country + " is not applicable please enter another country");
                    System.out.print((i+1) + " point: ");
                    country = in.nextLine().toUpperCase();
                    finalist = new Country(country);
                    pais = finalists.find(finalist);
                }
                Participant p = (Participant) pais.value();
                f[p.getID()].setPoints(i+1);
                finalists.remove(pais.value());
            }   
        }
    }
    
    private static BST loadFinalists(Participant[] f, Country voter)
    {
        BST result = new BST();
        for (int i = 0; i < f.length; i++)
        {
            if (f[i].getName().equals(voter.getName()))
            {
                continue;
            }
            else
            {
                result.insert(f[i]);
            }
        }
        return result;
    }
    
    private static void chooseP(Country[] p, Participant[] f, Country[] total, Scanner in, BST valid, boolean is, Random rand)
    {
        if (is)
        {
            for (int i = f.length; i < p.length; i++)
            {
                Country country = total[rand.nextInt(50)];
                while (valid.find(country) == null)
                {
                    country = total[rand.nextInt(50)];
                }
                valid.remove(country);
                p[i] = country;
            }
        }
        else
        {
            for (int i = f.length; i < p.length; i++)
            {
                System.out.print("Enter Country " + (i+1) + "/" + p.length + ": ");
                String country = in.nextLine().toUpperCase();
                Country finalist = new Country(country);
                BSTNode pais = valid.find(finalist);
                while (pais == null)
                {
                    System.out.println(country + " is not applicable please enter another country");
                    System.out.print("Enter Country " + (i+1) + "/" + p.length + ": ");
                    country = in.nextLine().toUpperCase();
                    finalist = new Country(country);
                    pais = valid.find(finalist);
                }
                finalist.setGreeting(pais.value().greeting());
                p[i] = finalist;
            }   
        }
    }
    
    private static void chooseF(Participant[] f, Country[] total, Scanner in, BST valid, boolean is, Random rand)
    {
        if (is)
        {
            for (int i = 0; i < f.length; i++)
            {
                Country country = total[rand.nextInt(50)];
                Participant finalist = new Participant(country.getName(), i);
                BSTNode pais = valid.find(finalist);
                while (pais == null)
                {
                    country = total[rand.nextInt(50)];
                    finalist = new Participant(country.getName(), i);
                    pais = valid.find(finalist);
                }
                finalist.setAcronym(pais.value().getAcronym());
                finalist.setGreeting(pais.value().greeting());
                valid.remove(finalist);
                f[i] = finalist;
            }
        }
        else
        {
            for (int i = 0; i < f.length; i++)
            {
                System.out.print("Enter Country " + (i+1) + "/" + f.length + ": ");
                String country = in.nextLine().toUpperCase();
                Participant finalist = new Participant(country, i);
                BSTNode pais = valid.find(finalist);
                while (pais == null)
                {
                    System.out.println(country + " is not applicable please enter another country");
                    System.out.print("Enter Country " + (i+1) + "/" + f.length + ": ");
                    country = in.nextLine().toUpperCase();
                    finalist = new Participant(country, i);
                    pais = valid.find(finalist);
                }
                finalist.setAcronym(pais.value().getAcronym());
                finalist.setGreeting(pais.value().greeting());
                valid.remove(finalist);
                f[i] = finalist;
            }   
        }
    }
    
    private static String line(Country c)
    {
        Random random = new Random();
        int n = random.nextInt(3);
        if (n == 0)
        {
            return c.getName() + " calling!\nWhat a great show! Here are the results from our jury:";
        }
        else if (n == 1)
        {
            return c.getName() + " calling!\nThank you for the wonderful show! ";
        }
        else
        {
            return c.getName() + " calling!\nFirst of all congrats on the amazing show! Now the votes:";
        }
    }
    
    private static BST setupAcronym(Country[] total) throws IOException
    {
        BST fSearch = new BST();
        File titles = new File("C:\\Users\\danil\\eclipse-workspace\\CS2114\\EurovisionSimulator\\escTitles.txt");
        File acronyms = new File("C:\\Users\\danil\\eclipse-workspace\\CS2114\\EurovisionSimulator\\escAcr.txt");
        File greetings = new File("C:\\Users\\danil\\eclipse-workspace\\CS2114\\EurovisionSimulator\\escGreetings.txt");
        Scanner inT = new Scanner(titles);
        Scanner inA = new Scanner(acronyms);
        Scanner inG = new Scanner(greetings, "CESU8");
        int count = 0;
        while (inT.hasNextLine() && inA.hasNextLine())
        {
            String name = inT.nextLine().toUpperCase();
            String a = inA.nextLine();
            Country c = new Country(name);
            c.setAcronym(a);
            fSearch.insert(c);
            total[count] = c;
            c.setGreeting(inG.nextLine());
            count++;
        }
        inT.close();
        inA.close();
        inG.close();
        return fSearch;
    }
}
