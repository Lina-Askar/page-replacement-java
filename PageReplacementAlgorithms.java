package PageReplacementAlgorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class PageReplacementAlgorithms {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of Frames? ");
        int frameNum = sc.nextInt();

        int[] pageNum = new int[20];
        System.out.println("Enter the page reference string, 20 each one at a time? ");
        for (int i = 0; i < pageNum.length; i++) {
            System.out.println("Enter page " + (i + 1) + ": ");
            pageNum[i] = sc.nextInt();

        }

        FIFO(frameNum, pageNum);
        System.out.println("\n \n");
        LRU(frameNum, pageNum);
        System.out.println("\n \n");
        Optimal(frameNum, pageNum);
        System.out.println("\n \n");
        analyzeBestAlgorithm(frameNum, pageNum);

    }

    private static void FIFO(int f, int[] p) {
        Queue<Integer> n = new LinkedList<>();
        Set<Integer> memory=new HashSet<>();

        int FIFOtotalPageFault = 0;
        int faultWithRep = 0;
        int faultWithoutRep = 0;

        System.out.println("FIRST IN FIRST OUT ALGORITHM:");

        for (Integer pa : p) {
            if (!memory.contains(pa)) {
                FIFOtotalPageFault++;

                if (n.size() == f) {
                    int removed=n.poll();
                    memory.remove(removed);
                    faultWithRep++;
                } else {
                    faultWithoutRep++;
                }

                n.add(pa);
                memory.add(pa);

            }
            PrintMemory(memory);
        }

        System.out.println("Page faults with replacements: " + faultWithRep);
        System.out.println("Page faults without replacements: " + faultWithoutRep);
        System.out.println("Total page faults: " + FIFOtotalPageFault);

    }

    private static void LRU(int f, int[] p) {
        Stack<Integer> memory = new Stack<>();

        int LRUtotalPageFault = 0;
        int faultWithRep = 0;
        int faultWithoutRep = 0;

        System.out.println("LEAST RECENTLY USED ALGORITHM:");

        for (int pa : p) {
            if (!memory.contains(pa)) {
                LRUtotalPageFault++;

                if (memory.size() == f) {
                    memory.remove(0);
                    faultWithRep++;
                } else {
                    faultWithoutRep++;
                }
            } else {
                memory.removeElement(pa);
            }
            memory.push(pa);
            PrintMemory(memory);
        }

        System.out.println("Page faults with replacements: " + faultWithRep);
        System.out.println("Page faults without replacements: " + faultWithoutRep);
        System.out.println("Total page faults: " + LRUtotalPageFault);

    }

    private static void Optimal(int f, int[] p) {
        List<Integer> memory = new ArrayList<>();

        int OptimaltotalPageFault = 0;
        int faultWithRep = 0;
        int faultWithoutRep = 0;

        System.out.println("OPTIMAL ALGORITHM:");

        for (int i = 0; i < p.length; i++) {
            int pa = p[i];
            if (!memory.contains(pa)) {
                OptimaltotalPageFault++;

                if (memory.size() == f) {
                    int far = -1;
                    int replace = -1;
                    for (int j = 0; j < memory.size(); j++) {
                        int useAgain = Integer.MAX_VALUE;
                        for (int k = i + 1; k < p.length; k++) {
                            if (p[k] == memory.get(j)) {
                                useAgain = k;
                                break;
                            }

                        }
                        if (useAgain > far) {
                            far = useAgain;
                            replace = j;
                        }
                    }
                    memory.remove(replace);
                    faultWithRep++;
                } else {
                    faultWithoutRep++;
                }
                memory.add(pa);
            }

            PrintMemory(memory);
        }

        System.out.println("Page faults with replacements: " + faultWithRep);
        System.out.println("Page faults without replacements: " + faultWithoutRep);
        System.out.println("Total page faults: " + OptimaltotalPageFault);
    }

    private static void PrintMemory(Collection<Integer> m) {
        System.out.println(m);
    }

    private static void analyzeBestAlgorithm(int f, int[] p) {

        int fifoPF = getPageFaults(f, p, "FIFO");
        int lruPF = getPageFaults(f, p, "LRU");
        int optimalPF = getPageFaults(f, p, "Optimal");

        String best = (fifoPF <= lruPF && fifoPF <= optimalPF) ? "FIFO ALGORITHM"
                : (lruPF <= optimalPF && lruPF <= fifoPF) ? "LRU ALGORITHM" : "OPTIMAL ALGORITHM";

        System.out.println("Best Algorithm based on the total number of replacements is: " + best);

    }

    private static int getPageFaults(int f, int[] p, String algo) {
        int pf = 0;

        if (algo.equals("FIFO")) {
            Queue<Integer> memory = new LinkedList<>();

            for (int page : p) {
                if (!memory.contains(page)) {
                    pf++;
                    if (memory.size() == f) {
                        memory.poll();
                    }
                    memory.add(page);

                }
            }
        } else if (algo.equals("LRU")) {
            Stack<Integer> memory = new Stack<>();
            for (int pa : p) {
                if (!memory.contains(pa)) {
                    pf++;
                    if (memory.size() == f) {
                        memory.remove(0);
                    }
                } else {
                    memory.removeElement(pa);
                }
                memory.add(pa);
            }
        } else if (algo.equals("Optimal")) {
            List<Integer> memory = new ArrayList<>();
            for (int i = 0; i < p.length; i++) {
                int pa = p[i];
                if (!memory.contains(pa)) {
                    pf++;
                    if (memory.size() == f) {
                        int far = -1, replace = -1;
                        for (int j = 0; j < memory.size(); j++) {
                            int useAgain = Integer.MAX_VALUE;
                            for (int k = i + 1; k < p.length; k++) {
                                if (p[k] == memory.get(j)) {
                                    useAgain = k;
                                    break;
                                }
                            }
                            if (useAgain > far) {
                                far = useAgain;
                                replace = j;
                            }
                        }
                        memory.remove(replace);
                    }
                    memory.add(pa);
                }
            }
        }
        return pf;
    }
}
