package io.github.flyingcowmoomoo.arrowcatsfpguild.java;

import java.util.ArrayList;
import java.util.List;

public class MyApi implements Api {

    private List<Integer> numbers;

    public MyApi() {
        this.numbers = new ArrayList<>();
    }

    @Override
    public List<Integer> getNumbers() {
        return this.numbers;
    }

    @Override
    public boolean addNumbers(List<Integer> numbers) {
        if(this.numbers.stream().anyMatch(numbers::contains)) {
            return false;
        }
        else {
            return this.numbers.addAll(numbers);
        }
    }

    @Override
    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}

interface Api {
    List<Integer> getNumbers();
    boolean addNumbers(List<Integer> numbers);
    void setNumbers(List<Integer> numbers);
}
