package edu.eci.arsw.primefinder;

import java.util.*;

public class PrimeFinderThread extends Thread {

    int a, b;

    /**
     * List with the prime numbers found
     */
    private List<Integer> primes;
    /**
     * Size of the prime list in a particular moment
     */
    private int mySize;

    /**
     * Create a thread with a range and an integer list, this thread search prime
     * numbers in the range and add it to the list.
     * 
     * @param a         Lowest number in the range
     * @param b         Highest number in the range
     * @param primeList List where the prime numbers have to be added
     */
    public PrimeFinderThread(int a, int b, List<Integer> primeList) {
        super();
        this.primes = primeList;
        this.a = a;
        this.b = b;
        this.mySize = primeList.size();
    }

    /**
     * Pass through the range checking if is a prime number. After checking all the
     * range, the thread pass to interrupt state.
     */
    @Override
    public void run() {
        for (int i = a; i < b; i++) {
            if (isPrime(i)) {
                try {
                    addPrime(i);
                } catch (InterruptedException ex) {
                }
            }
        }
        this.interrupt();
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

    /**
     * Control the concurrent access to the prime list to verify if the actual
     * thread is the only instance that is modifying the list. If it is, the prime
     * number will be added. Otherwise, the thread wait until other thread notify
     * that the list is release.
     * 
     * @param number The prime number to add in the list
     * @throws InterruptedException If went wrong with the wait() method
     */
    private void addPrime(int number) throws InterruptedException {
        synchronized (this.primes) {
            if (this.primes.size() != mySize) {
                this.primes.wait();
            }

            primes.add(number);
            mySize = this.primes.size();
            this.primes.notifyAll();
        }
    }

    public List<Integer> getPrimes() {
        return primes;
    }
}
