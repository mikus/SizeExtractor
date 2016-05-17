package net.mikus.assignment.comparator;


import java.util.Comparator;

public class SimpleGenericComparator<T> implements Comparator<T> {

    @Override
    @SuppressWarnings("unchecked")
    public int compare(T o1, T o2) {
        try {
            if (o1 instanceof Comparable)
                return ((Comparable<T>) o1).compareTo(o2);
            if (o2 instanceof Comparable)
                return -((Comparable<T>) o2).compareTo(o1);
        } catch (ClassCastException ignored) { }
        return o1.toString().compareTo(o2.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SimpleGenericComparator;
    }
}
