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
    private List<Integer> primeList;

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

    private boolean areThreadsAlive() {
        for (PrimeFinderThread t : this.pft) {
            if (!t.isInterrupted()) {
                return true;
            }
        }
        return false;
    }

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
