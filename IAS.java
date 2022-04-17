import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IAS {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String inp = "START";
        String regMQ = "";
        String regAC = "";
        int counter = 0;
        HashMap<String, String> usableMemory = new HashMap<>();
        usableMemory.put("0F9","");
        usableMemory.put("0FA","");
        usableMemory.put("0FB","");
        usableMemory.put("0FC","");
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("LOAD");
        keywords.add("STOR");
        keywords.add("JUMP");
        keywords.add("JUMP+");
        keywords.add("ADD");
        keywords.add("SUB");
        keywords.add("MUL");
        keywords.add("DIV");
        keywords.add("LSH");
        keywords.add("RSH");
        keywords.add("MOV");
        keywords.add("EXIT");
        int programCounter = 0;

        File file = new File(System.getProperty("user.dir")+"/Program.txt");
        Scanner sc = new Scanner(file);
        HashMap<Integer, String> instructionList = new HashMap<>();
        int i = 0;
        while (sc.hasNextLine()){
            String nextLine = sc.nextLine();
            int end = nextLine.length();
            if (nextLine.indexOf("//") > -1)
                end = nextLine.indexOf("//");
            instructionList.put(i, nextLine.substring(0, end));
            ++i;
        }

        while (!instructionList.get(programCounter).trim().equals("EXIT")) {
            inp = instructionList.get(programCounter);
            Object[] outputs = execute(inp, keywords,regAC,regMQ,usableMemory, programCounter);
            keywords = (ArrayList<String>) outputs[0];
            regAC = (String) outputs[1];
            regMQ = (String) outputs[2];
            usableMemory = (HashMap<String, String>) outputs[3];
            programCounter = (int) outputs[4];
        }

        System.out.println("AC: " + regAC + " | " + "MQ: " + regMQ);
        String printable = "";
        for (String x : usableMemory.keySet()) {
            printable += x + ": " + usableMemory.get(x) + " | ";
        }
        System.out.println(printable);
    }
    public static Object[] execute(String inp, ArrayList<String> keywords, String regAC, String regMQ, HashMap<String, String> usableMemory, int programCounter) {
        String[] argz = inp.split(" ");
        argz[0] = argz[0].trim();
        if (argz.length > 1)
            argz[1] = argz[1].trim();
        programCounter += 1;
        if (!keywords.contains(argz[0])) { throw new IllegalArgumentException(); }

        if (argz[0].equals("LOAD")) {
            if (argz[1].equals("MQ")) {
                regAC = regMQ;
            }
            if (argz[1].startsWith("MQ,M(")) {
                String loc = argz[1].substring(5, argz[1].length()-1);
                regMQ = usableMemory.get(loc);
            }
            if (argz[1].startsWith("M(")) {
                String loc = argz[1].substring(2, argz[1].length()-1);
                regAC = usableMemory.get(loc);
            }
            if (argz[1].startsWith("-M(")) {
                String loc = argz[1].substring(3, argz[1].length()-1);
                regAC = "-" + usableMemory.get(loc);
            }
            if (argz[1].startsWith("|M(")) {
                String loc = argz[1].substring(2, argz[1].length()-1);
                regAC = String.valueOf(Math.abs(Integer.parseInt(usableMemory.get(loc))));
            }
            if (argz[1].startsWith("-|M(")) {
                String loc = argz[1].substring(2, argz[1].length()-1);
                regAC = String.valueOf(-1*Math.abs(Integer.parseInt(usableMemory.get(loc))));
            }
        }
        if (argz[0].equals("STOR")) {
            usableMemory.replace(argz[1].substring(2, argz[1].length() - 1), regAC);
        }
        if (argz[0].equals("ADD")) {
            if (argz[1].startsWith("M(")) {
                regAC = String.valueOf(Integer.parseInt(regAC) + Integer.parseInt(usableMemory.get(argz[1].substring(2, argz[1].length() - 1))));
            }
            if (argz[1].startsWith("|M(")) {
                regAC = String.valueOf(Integer.parseInt(regAC) + Math.abs(Integer.parseInt(usableMemory.get(argz[1].substring(2, argz[1].length() - 1)))));
            }
        }
        if (argz[0].equals("SUB")) {
            if (argz[1].startsWith("M(")) {
                regAC = String.valueOf(Integer.parseInt(regAC) - Integer.parseInt(usableMemory.get(argz[1].substring(2, argz[1].length() - 1))));
            }
            if (argz[1].startsWith("|M(")) {
                regAC = String.valueOf(Integer.parseInt(regAC) - Math.abs(Integer.parseInt(usableMemory.get(argz[1].substring(2, argz[1].length() - 1)))));
            }
        }
        if (argz[0].equals("MUL")) {
            String result = Integer.toBinaryString(Integer.parseInt(regMQ) * Integer.parseInt(usableMemory.get(argz[1].substring(2, argz[1].length() - 1))));
            result = String.format("%40s", result).replaceAll(" ", "0");
            regAC = String.valueOf(Integer.parseInt(result.substring(0, result.length()/2), 2));
            regMQ = String.valueOf(Integer.parseInt(result.substring(result.length()/2), 2));
        }
        if (argz[0].equals("DIV")) {
            regMQ = String.valueOf(Integer.parseInt(regAC) / Integer.parseInt(usableMemory.get(argz[1].substring(2, argz[1].length() - 1))));
            regAC = String.valueOf(Integer.parseInt(regAC) % Integer.parseInt(usableMemory.get(argz[1].substring(2, argz[1].length() - 1))));
        }
        if (argz[0].equals("LSH")) {
            regAC = String.valueOf(Integer.parseInt(regAC) * 2);
        }
        if (argz[0].equals("RSH")) {
            regAC = String.valueOf(Integer.parseInt(regAC) / 2);
        }
        if (argz[0].equals("MOV")) {
            regAC = String.valueOf(Integer.parseInt(argz[1]));
        }
        if (argz[0].equals("JUMP")) {
            programCounter = Integer.parseInt(argz[1].substring(2, argz[1].length() - 1));
        }
        if (argz[0].equals("JUMP+")) {
            if (Integer.parseInt(regAC) >= 0) {
                programCounter = Integer.parseInt(argz[1].substring(2, argz[1].length() - 1));
            }
        }
        return new Object[]{keywords, regAC, regMQ, usableMemory, programCounter};
    }
}
