package net.mikus.assignment.comparator

import spock.lang.Specification


class SimpleGenericComparatorSpec extends Specification {

    SimpleGenericComparator comparator = new SimpleGenericComparator()

    def "Comparator should compare explicitly comparable types"() {
        when:
        data.sort(comparator)

        then:
        data == result

        where:
             data       || result
        [3, 4, 2, 1]    || [1, 2, 3, 4]
        ['a', 'c', 'b'] || ['a', 'b', 'c']
    }

    def "Comparator should compare implicitly comparable types"() {
        when:
        data.sort(comparator)

        then:
        data == result

        where:
                           data                                        || result
        [new MyOtherClass(1), new MyOtherClass(3), new MyIntClass(2)]  || [new MyOtherClass(1), new MyIntClass(2), new MyOtherClass(3)]
    }


    def "Comparator should compare not comparable types"() {
        when:
        data.sort(comparator)

        then:
        data == result

        where:
                               data                                                                   || result
        [new MyOtherClass(1), new MyStringClass('a'), new MyStringClass('c'), new MyStringClass('b')] || [new MyStringClass('a'), new MyStringClass('b'), new MyStringClass('c'), new MyOtherClass(1)]
    }

}

class MyStringClass {

    private final String data

    MyStringClass(String data) { this.data = data }

    @Override
    String toString() { return data }

    @Override
    boolean equals(Object obj) {
        return obj instanceof MyStringClass && obj.data == data
    }

}

class MyIntClass {

    protected final int data

    MyIntClass(int data) { this.data = data }

    @Override
    boolean equals(Object obj) {
        return obj instanceof MyIntClass && obj.data == data
    }

}

class MyOtherClass extends MyIntClass implements Comparable<MyIntClass> {


    MyOtherClass(int data) {
        super(data)
    }

    @Override
    int compareTo(MyIntClass o) {
        return data - o.data
    }
}