package com.carlkuesters.fifachampions.menu;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldPlayerContainer {
    private Container container;
    private Label lblSkill;
    private Label lblName;
    private MenuElement menuElement;
}
