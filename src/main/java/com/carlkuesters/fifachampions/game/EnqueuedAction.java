package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class EnqueuedAction {
    private Runnable runnable;
    @Setter
    private float remainingDelay;
}
