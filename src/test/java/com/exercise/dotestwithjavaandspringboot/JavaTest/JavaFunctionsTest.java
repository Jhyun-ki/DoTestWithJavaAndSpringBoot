package com.exercise.dotestwithjavaandspringboot.JavaTest;

import com.jayway.jsonpath.internal.function.numeric.Sum;
import lombok.Data;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.sql.In;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JavaFunctionsTest {

    @Test
    public void streamLambdaExercise() {
        //given
        List<String> list = Arrays.asList("AAA", "BBB", "CCC");
        List<Student> listStd = Arrays.asList(
                new Student("AAA", 90),
                new Student("BBB", 80),
                new Student("CCC", 77)
        );
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);

        System.out.println("===========스트림 사용 안한 버전===========");
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("=======람다 사용(스트림 사용 안함)=======");

        System.out.println("===========스트림 사용===========");
        Stream<String> stream = list.stream();
        stream.forEach( a ->System.out.println(a));

        System.out.println("===========스트림 사용===========");
        listStd.stream().forEach(s -> {
            System.out.println(s.getName() + "-" + s.getScore());
        });

        System.out.println("===========병렬 처리===========");
        System.out.println("===========아래의 코드는 동시다발적(병렬)으로 실행되기 때문에 항상 순서가 달라진다.(순서를 개발자가 통제 불가)===========");
        listOfNumbers.parallelStream().forEach(number -> System.out.println(number + " " + Thread.currentThread().getName()));


        System.out.println("===========맵핑, 필터링, 평균 기능 적용 ===========");
        double avg = listStd.stream()
                    .mapToInt(Student::getScore)
                    .average()
                    .getAsDouble();
        System.out.println("평균 점수 : " + avg);

        System.out.println("===========소스 분할 ===========");
        //fork-join 프레임워크는 worker 스레드간에 소스 데이터를 분할하고 작업 완료시 콜백을 처리하는 역할을 함.
        int sum = listOfNumbers.stream().reduce(5, Integer::sum); // 순차 스트림 > 15
        int sumParallel = listOfNumbers.parallelStream().reduce(5, Integer::sum); // reduce 작업이 병렬로 처리되기 때문에 모든 스레드에서 5를 더하게 됨으로 합이 15가 되지 않는다.
        int sumParallelExternal = listOfNumbers.parallelStream().reduce(0, Integer::sum) + 5; // 이런식으로 병렬 스트림 외부에 +5를 해줘야한다.
        System.out.println("순차 stream sum = " + sum);
        System.out.println("병렬 stream sum = " + sumParallel);
        System.out.println("병렬 stream 외부에서 sum = " + sumParallelExternal);

        assertThat(sum).isEqualTo(15);
        assertThat(sumParallel).isNotEqualTo(15);
        assertThat(sumParallelExternal).isEqualTo(15);

    }

}



@Getter
class Student {
    private String name;
    private int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }
}