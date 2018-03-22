package me.etki.tasks.revolving.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Page<T> {
    @Getter
    private final List<T> content;
}
