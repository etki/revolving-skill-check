package me.etki.tasks.revolving.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
public class Page<T> {
    @Getter
    private final List<T> content;

    @Getter
    private final Metadata metadata;

    @Accessors(chain = true)
    public static class Metadata {
        @Getter
        @Setter
        private int total;
        @Getter
        @Setter
        private int size;
        @Getter
        @Setter
        private int pages;
        @Getter
        @Setter
        private int page;

        public static Metadata from(int page, int size, int total) {
            return new Metadata()
                    .setPage(page)
                    .setSize(size)
                    .setTotal(total)
                    .setPages((total + size - 1) / size);
        }
    }
}
