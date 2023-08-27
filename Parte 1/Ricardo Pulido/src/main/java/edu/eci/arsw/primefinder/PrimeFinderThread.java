package edu.eci.arsw.primefinder;

import java.util.*;

public class PrimeFinderThread extends Thread {

    int a, b;

    private List<Integer> primes;
    private int mySize;
    private int TMILISECONDS;

    public PrimeFinderThread(int a, int b, List<Integer> primeList, int TMILISECONDS) {
        super();
        this.primes = primeList;
        this.a = a;
        this.b = b;
        this.mySize = primeList.size();
        this.TMILISECONDS = TMILISECONDS;
    }

    @Override
    public void run() {
        for (int i = a; i < b; i++) {
            if (isPrime(i)) {
                try {
                    addPrime(i);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n % 2 != 0;
            for (int i = 3; ans && i * i <= n; i += 2) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
    }

    private void addPrime(int number) throws InterruptedException {
        synchronized (this.primes) {
            while (this.primes.size() != mySize) {
                this.primes.wait();
            }

            Thread.sleep(this.TMILISECONDS);
            System.out.println(number);
            mySize = this.primes.size();
            this.primes.notifyAll();
        }
    }

    public List<Integer> getPrimes() {
        return primes;
    }
}
