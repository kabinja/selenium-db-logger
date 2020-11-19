package tech.ikora.socket.server.difference;

import tech.ikora.diff.parser.DiffParser;
import tech.ikora.diff.parser.MalformedDiffException;
import tech.ikora.diff.patch.Patch;
import tech.ikora.diff.patch.Patches;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StackTraceMapper {
    public static String map(String newStackTrace, String diff){
        try {
            final Patches patches = new DiffParser().parse(diff);
            final List<String> stack = Arrays.stream(newStackTrace.split(";"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            return String.join(";", map(stack, patches));
        } catch (IOException | MalformedDiffException e) {
            return "";
        }
    }

    private static List<String> map(List<String> stack, Patches patches){
        return stack.stream()
                .map(s -> computeLine(s, patches))
                .collect(Collectors.toList());
    }

    private static String computeLine(String old, Patches patches){
        final Optional<Patch> patch = patches.getByNewFile(old, false);

        if(patch.isEmpty()){
            return old;
        }

        final int index = old.lastIndexOf(":");
        final int line = Integer.parseInt(old.substring(index));

        return old;
    }
}
