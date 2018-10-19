package mycontroller.util;

/**
 * Helper Data Structures that stores objects of two generic data types: first and second
 * 
 * Code written by Peter Lawrey 
 * Sourced from Stack Overflow
 * (https://stackoverflow.com/questions/156275/what-is-the-equivalent-of-the-c-pairl-r-in-java)
 * */
public class Pair<T1, T2> {
	private T1 first; //first element of generic type
    private T2 second; //first element of generic type

    /**
     * Constructor for Pair
     * @param first - first element of generic type
     * @param second - second element of generic type
     * */
    public Pair(T1 first, T2 second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     * Returns hash code of pair
     * */
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    /**
     * Identifies if object is equal to current pair instance 
     * @param other - object to be compared 
     * */
    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return 
            ((  this.first == otherPair.first ||
                ( this.first != null && otherPair.first != null &&
                  this.first.equals(otherPair.first))) &&
             (  this.second == otherPair.second ||
                ( this.second != null && otherPair.second != null &&
                  this.second.equals(otherPair.second))) );
        }

        return false;
    }

    /**
     * Returns string representing current pair
     * */
    public String toString()
    { 
           return "(" + first + ", " + second + ")"; 
    }

    /**
     * Returns first element
     * */
    public T1 getFirst() {
        return first;
    }

    /**
     * Sets value of first element
     * */
    public void setFirst(T1 first) {
        this.first = first;
    }

    /**
     * Returns second element
     * */
    public T2 getSecond() {
        return second;
    }

    /**
     * Sets value of second element
     * */
    public void setSecond(T2 second) {
        this.second = second;
    }
}
