package com.carlkuesters.fifachampions.menu;

import com.simsilica.lemur.Panel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuElement {
    private Panel panel;
    private Runnable action;
}
