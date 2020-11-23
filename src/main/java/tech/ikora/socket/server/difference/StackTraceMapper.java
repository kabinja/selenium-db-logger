package tech.ikora.socket.server.difference;

import tech.ikora.diff.parser.DiffParser;
import tech.ikora.diff.parser.MalformedDiffException;
import tech.ikora.diff.patch.Change;
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
        if(old.isBlank()){
            return old;
        }

        final String[] split = old.split(":");

        final String file = split[0].replace('.', '/') + ".java";
        final int line = Integer.parseInt(split[2]);

        final Optional<Patch> patch = patches.getByNewFile(file, false);

        if(patch.isEmpty()){
            return old;
        }

        final long add = countPositions(patch.get(), line, Change.Type.ADD);
        final long remove = countPositions(patch.get(), line, Change.Type.REMOVE);

        final long newLine = line - add + remove;

        return split[0] + ":" + split[1] + ":" + newLine;
    }

    private static long countPositions(Patch patch, int line, Change.Type type){
        return patch.getHunks().stream()
                .flatMap(h -> h.getChanges().stream())
                .filter(c -> c.getType() == type)
                .map(Change::getPosition)
                .filter(p -> p < line)
                .count();
    }
}
