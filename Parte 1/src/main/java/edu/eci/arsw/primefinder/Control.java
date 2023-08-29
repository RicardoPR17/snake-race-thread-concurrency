/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 *
 */
public class Control extends Thread {

    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    public final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    /**
     * List that contains the primes found by the threads
     */
    private List<Integer> primeList;

    /**
     * Constructor of the Control's class, create threads to divide the search of
     * prime numbers on a range concurrently
     */
    private Control() {
        super();
        this.pft = new PrimeFinderThread[NTHREADS];
        primeList = new LinkedList<>();

        int i;
        for (i = 0; i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA, primeList);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i * NDATA, MAXVALUE + 1, primeList);
    }

    public static Control newControl() {
        return new Control();
    }

    /**
     * Check if the created threads are interrupted or not
     * 
     * @return true if at least one thread are running, false otherwise
     */
    private boolean areThreadsAlive() {
        for (PrimeFinderThread t : this.pft) {
            if (!t.isInterrupted()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Start the execution of the threads that find the prime numbers on a given
     * range. Every 5 seconds, this threads stop his execution and the amount of
     * prime numbers found are shown. The threads continue the searching after a
     * keyboard input. This is going to happen until last number in the range is
     * checked.
     */
    @Override
    public void run() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < NTHREADS; i++) {
            pft[i].start();
        }

        int primesCount;

        boolean supervisor = areThreadsAlive();

        while (supervisor) {
            long start = System.currentTimeMillis();
            long end = start + TMILISECONDS;
            while (start <= end) {
                start = System.currentTimeMillis();
            }

            synchronized (primeList) {
                primesCount = primeList.size();
                System.out.println("Cantidad de primos encontrados: " + primesCount);

                try {
                    input.readLine();
                } catch (IOException ioE) {
                }

                primeList.notifyAll();
            }
            supervisor = areThreadsAlive();
        }
    }
}
