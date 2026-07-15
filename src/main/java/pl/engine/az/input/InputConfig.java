package pl.engine.az.input;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class InputConfig {
    private Map<Action, List<Integer>> bindings;
}
