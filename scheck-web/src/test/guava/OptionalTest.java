package guava;

import com.google.common.base.Optional;

/**
 * Created by dell on 2017/7/12.
 */
public class OptionalTest {

    public static void main(String[] args) {
        Optional<String> name = Optional.of("");
        if(name.isPresent()) {
            System.out.println(name.get());
        }
    }

}
